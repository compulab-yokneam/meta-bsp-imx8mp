#!/bin/sh

set -eu

ISP_CAPTURE_WIDTH=${ISP_CAPTURE_WIDTH:-1920}
ISP_CAPTURE_HEIGHT=${ISP_CAPTURE_HEIGHT:-1080}
ISP_CAPTURE_FPS=${ISP_CAPTURE_FPS:-30}
ISP_VIDEO_FORMAT=${ISP_VIDEO_FORMAT:-NV12}
ISP_DISPLAY_WIDTH=${ISP_DISPLAY_WIDTH:-1024}
ISP_DISPLAY_HEIGHT=${ISP_DISPLAY_HEIGHT:-600}
ISP_GST_SINK=${ISP_GST_SINK:-waylandsink}

usage()
{
	cat <<EOF
Usage: ${0##*/} [OPTION] [CAMERA]

Device discovery:
  --shell              Print CSI1_VIDEO and CSI2_VIDEO assignments (default)
  --csi1               Print only the CSI1/ISP0 capture device
  --csi2               Print only the CSI2/ISP1 capture device

GStreamer pipelines:
  --gst-single CAMERA   Print a single-camera pipeline (CAMERA: csi1 or csi2)
  --run-single CAMERA   Run a single-camera pipeline
  --gst-dual            Print the dual-camera pipeline
  --run-dual            Run the dual-camera pipeline

Pipeline settings can be overridden with ISP_CAPTURE_WIDTH,
ISP_CAPTURE_HEIGHT, ISP_CAPTURE_FPS, ISP_VIDEO_FORMAT, ISP_DISPLAY_WIDTH,
ISP_DISPLAY_HEIGHT, and ISP_GST_SINK.
EOF
}

find_video_by_bus()
{
	wanted_bus=$1

	if ! command -v v4l2-ctl >/dev/null 2>&1; then
		echo "${0##*/}: v4l2-ctl is not installed" >&2
		return 1
	fi

	for video_device in /dev/video*; do
		[ -e "$video_device" ] || continue

		if v4l2-ctl --device="$video_device" --info 2>/dev/null |
			grep -Eq "^[[:space:]]*Bus info[[:space:]]*:[[:space:]]*platform:${wanted_bus}[[:space:]]*$"; then
			printf '%s\n' "$video_device"
			return 0
		fi
	done

	return 1
}

get_camera_device()
{
	case "$1" in
		csi1)
			if [ -n "${CSI1_VIDEO:-}" ]; then
				printf '%s\n' "$CSI1_VIDEO"
			else
				find_video_by_bus viv0
			fi
			;;
		csi2)
			if [ -n "${CSI2_VIDEO:-}" ]; then
				printf '%s\n' "$CSI2_VIDEO"
			else
				find_video_by_bus viv1
			fi
			;;
		*)
			echo "${0##*/}: camera must be csi1 or csi2" >&2
			return 1
			;;
	esac
}

get_required_camera_device()
{
	camera_name=$1

	camera_device=$(get_camera_device "$camera_name") || {
		echo "${0##*/}: ${camera_name} VIV device was not found" >&2
		exit 1
	}

	printf '%s\n' "$camera_device"
}

validate_positive_integer()
{
	case "$2" in
		''|*[!0-9]*)
			echo "${0##*/}: $1 must be a positive integer" >&2
			exit 2
			;;
	esac

	if [ "$2" -eq 0 ]; then
		echo "${0##*/}: $1 must be greater than zero" >&2
		exit 2
	fi
}

validate_pipeline_settings()
{
	validate_positive_integer ISP_CAPTURE_WIDTH "$ISP_CAPTURE_WIDTH"
	validate_positive_integer ISP_CAPTURE_HEIGHT "$ISP_CAPTURE_HEIGHT"
	validate_positive_integer ISP_CAPTURE_FPS "$ISP_CAPTURE_FPS"
	validate_positive_integer ISP_DISPLAY_WIDTH "$ISP_DISPLAY_WIDTH"
	validate_positive_integer ISP_DISPLAY_HEIGHT "$ISP_DISPLAY_HEIGHT"

	if [ "$ISP_DISPLAY_WIDTH" -lt 2 ]; then
		echo "${0##*/}: ISP_DISPLAY_WIDTH must be at least 2" >&2
		exit 2
	fi
}

require_gst_launch()
{
	if ! command -v gst-launch-1.0 >/dev/null 2>&1; then
		echo "${0##*/}: gst-launch-1.0 is not installed" >&2
		exit 1
	fi
}

print_single_pipeline()
{
	camera_device=$1

	cat <<EOF
gst-launch-1.0 -e \\
  imxcompositor_g2d name=comp \\
    sink_0::xpos=0 sink_0::ypos=0 \\
    sink_0::width=${ISP_DISPLAY_WIDTH} sink_0::height=${ISP_DISPLAY_HEIGHT} \\
  ! video/x-raw,width=${ISP_DISPLAY_WIDTH},height=${ISP_DISPLAY_HEIGHT} \\
  ! ${ISP_GST_SINK} sync=false \\
  v4l2src device=${camera_device} \\
  ! video/x-raw,format=${ISP_VIDEO_FORMAT},width=${ISP_CAPTURE_WIDTH},height=${ISP_CAPTURE_HEIGHT},framerate=${ISP_CAPTURE_FPS}/1 \\
  ! queue \\
  ! comp.sink_0
EOF
}

print_dual_pipeline()
{
	csi1_device=$1
	csi2_device=$2
	left_width=$((ISP_DISPLAY_WIDTH / 2))
	right_width=$((ISP_DISPLAY_WIDTH - left_width))

	cat <<EOF
gst-launch-1.0 -e \\
  imxcompositor_g2d name=comp \\
    sink_0::xpos=0 sink_0::ypos=0 \\
    sink_0::width=${left_width} sink_0::height=${ISP_DISPLAY_HEIGHT} \\
    sink_1::xpos=${left_width} sink_1::ypos=0 \\
    sink_1::width=${right_width} sink_1::height=${ISP_DISPLAY_HEIGHT} \\
  ! video/x-raw,width=${ISP_DISPLAY_WIDTH},height=${ISP_DISPLAY_HEIGHT} \\
  ! ${ISP_GST_SINK} sync=false \\
  v4l2src device=${csi1_device} \\
  ! video/x-raw,format=${ISP_VIDEO_FORMAT},width=${ISP_CAPTURE_WIDTH},height=${ISP_CAPTURE_HEIGHT},framerate=${ISP_CAPTURE_FPS}/1 \\
  ! queue \\
  ! comp.sink_0 \\
  v4l2src device=${csi2_device} \\
  ! video/x-raw,format=${ISP_VIDEO_FORMAT},width=${ISP_CAPTURE_WIDTH},height=${ISP_CAPTURE_HEIGHT},framerate=${ISP_CAPTURE_FPS}/1 \\
  ! queue \\
  ! comp.sink_1
EOF
}

run_single_pipeline()
{
	camera_device=$1

	exec gst-launch-1.0 -e \
		imxcompositor_g2d name=comp \
		sink_0::xpos=0 sink_0::ypos=0 \
		sink_0::width="$ISP_DISPLAY_WIDTH" \
		sink_0::height="$ISP_DISPLAY_HEIGHT" \
		! "video/x-raw,width=${ISP_DISPLAY_WIDTH},height=${ISP_DISPLAY_HEIGHT}" \
		! "$ISP_GST_SINK" sync=false \
		v4l2src device="$camera_device" \
		! "video/x-raw,format=${ISP_VIDEO_FORMAT},width=${ISP_CAPTURE_WIDTH},height=${ISP_CAPTURE_HEIGHT},framerate=${ISP_CAPTURE_FPS}/1" \
		! queue \
		! comp.sink_0
}

run_dual_pipeline()
{
	csi1_device=$1
	csi2_device=$2
	left_width=$((ISP_DISPLAY_WIDTH / 2))
	right_width=$((ISP_DISPLAY_WIDTH - left_width))

	exec gst-launch-1.0 -e \
		imxcompositor_g2d name=comp \
		sink_0::xpos=0 sink_0::ypos=0 \
		sink_0::width="$left_width" \
		sink_0::height="$ISP_DISPLAY_HEIGHT" \
		sink_1::xpos="$left_width" sink_1::ypos=0 \
		sink_1::width="$right_width" \
		sink_1::height="$ISP_DISPLAY_HEIGHT" \
		! "video/x-raw,width=${ISP_DISPLAY_WIDTH},height=${ISP_DISPLAY_HEIGHT}" \
		! "$ISP_GST_SINK" sync=false \
		v4l2src device="$csi1_device" \
		! "video/x-raw,format=${ISP_VIDEO_FORMAT},width=${ISP_CAPTURE_WIDTH},height=${ISP_CAPTURE_HEIGHT},framerate=${ISP_CAPTURE_FPS}/1" \
		! queue \
		! comp.sink_0 \
		v4l2src device="$csi2_device" \
		! "video/x-raw,format=${ISP_VIDEO_FORMAT},width=${ISP_CAPTURE_WIDTH},height=${ISP_CAPTURE_HEIGHT},framerate=${ISP_CAPTURE_FPS}/1" \
		! queue \
		! comp.sink_1
}

mode=${1:---shell}

case "$mode" in
	--shell)
		[ "$#" -le 1 ] || {
			usage >&2
			exit 2
		}
		if [ -z "${CSI1_VIDEO:-}${CSI2_VIDEO:-}" ] &&
			! command -v v4l2-ctl >/dev/null 2>&1; then
			echo "${0##*/}: v4l2-ctl is not installed" >&2
			exit 1
		fi
		csi1_video=$(get_camera_device csi1 2>/dev/null) || csi1_video=
		csi2_video=$(get_camera_device csi2 2>/dev/null) || csi2_video=
		if [ -z "$csi1_video$csi2_video" ]; then
			echo "${0##*/}: no VIV camera devices were found" >&2
			exit 1
		fi
		printf "CSI1_VIDEO='%s'\n" "$csi1_video"
		printf "CSI2_VIDEO='%s'\n" "$csi2_video"
		;;
	--csi1)
		[ "$#" -eq 1 ] || {
			usage >&2
			exit 2
		}
		get_required_camera_device csi1
		;;
	--csi2)
		[ "$#" -eq 1 ] || {
			usage >&2
			exit 2
		}
		get_required_camera_device csi2
		;;
	--gst-single|--run-single)
		[ "$#" -eq 2 ] || {
			usage >&2
			exit 2
		}
		validate_pipeline_settings
		camera_device=$(get_required_camera_device "$2")
		if [ "$mode" = "--gst-single" ]; then
			print_single_pipeline "$camera_device"
		else
			require_gst_launch
			run_single_pipeline "$camera_device"
		fi
		;;
	--gst-dual|--run-dual)
		[ "$#" -eq 1 ] || {
			usage >&2
			exit 2
		}
		validate_pipeline_settings
		csi1_device=$(get_required_camera_device csi1)
		csi2_device=$(get_required_camera_device csi2)
		if [ "$mode" = "--gst-dual" ]; then
			print_dual_pipeline "$csi1_device" "$csi2_device"
		else
			require_gst_launch
			run_dual_pipeline "$csi1_device" "$csi2_device"
		fi
		;;
	-h|--help)
		usage
		;;
	*)
		usage >&2
		exit 2
		;;
esac
