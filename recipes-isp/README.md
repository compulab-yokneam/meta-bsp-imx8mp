# Optional ISP support

This directory contains optional camera and ISP integrations for the NXP
i.MX8MP ISP stack.

## Supported sensors

| Sensor | VVCAM module | ISP configuration | CSI1/ISP0 overlay | CSI2/ISP1 overlay |
| --- | --- | --- | --- | --- |
| Sony IMX219 | `imx219` | `imx219_1080p30` | `sbev-ucmimx8plus-csi1-isp0-imx219.dtbo` | `sbev-ucmimx8plus-csi2-isp1-imx219.dtbo` |
| OmniVision OV5647 | `ov5647` | `ov5647_1080p30` | `sbev-ucmimx8plus-csi1-isp0-ov5647.dtbo` | `sbev-ucmimx8plus-csi2-isp1-ov5647.dtbo` |

The `kernel-module-isp-vvcam` bbappend provides the sensor kernel modules.
The `isp-imx` bbappend provides the corresponding ISP drivers, calibration
data, runtime configurations, and automatic device-tree detection.

The overlays are built and deployed for the `ucm-imx8m-plus-sbev` machine.
Select one overlay for a single sensor or one overlay for each CSI interface
in a dual-sensor configuration.

Select the OV5647 overlay for CSI1/ISP0 with:

```shell
fw_setenv fdtofile sbev-ucmimx8plus-csi1-isp0-ov5647.dtbo
```

Select the OV5647 overlay for CSI2/ISP1 with:

```shell
fw_setenv fdtofile sbev-ucmimx8plus-csi2-isp1-ov5647.dtbo
```

Select the IMX219 overlay for CSI1/ISP0 with:

```shell
fw_setenv fdtofile sbev-ucmimx8plus-csi1-isp0-imx219.dtbo
```

Select the IMX219 overlay for CSI2/ISP1 with:

```shell
fw_setenv fdtofile sbev-ucmimx8plus-csi2-isp1-imx219.dtbo
```

## Dual-sensor configurations

The `dual_imx219_ov5647_1080p30` ISP configuration supports both mixed-sensor
layouts. The runtime reads each sensor's `csi_id`, writes the matching
`Sensor0_Entry.cfg` and `Sensor1_Entry.cfg`, and starts `isp_media_server` in
`DUAL_CAMERA` mode. Automatic ISP startup detects the mixed pair and selects
this configuration.

Multiple overlays are stored as a space-separated list in `fdtofile` and are
applied in the listed order.

Select IMX219 on CSI1/ISP0 and OV5647 on CSI2/ISP1 with:

```shell
fw_setenv fdtofile \
  'sbev-ucmimx8plus-csi1-isp0-imx219.dtbo sbev-ucmimx8plus-csi2-isp1-ov5647.dtbo'
```

Select OV5647 on CSI1/ISP0 and IMX219 on CSI2/ISP1 with:

```shell
fw_setenv fdtofile \
  'sbev-ucmimx8plus-csi1-isp0-ov5647.dtbo sbev-ucmimx8plus-csi2-isp1-imx219.dtbo'
```

Reboot the system after changing `fdtofile`. Setting the variable replaces the
previous sensor overlay list.

## Camera capture devices

Linux video device numbers depend on probe order and must not be assumed to
remain fixed. The `isp-camera-devices` helper identifies the camera capture
devices by their stable VIV platform bus names:

- `platform:viv0` is CSI1/ISP0.
- `platform:viv1` is CSI2/ISP1.

Load the detected device paths into the current shell with:

```shell
eval "$(isp-camera-devices)"
echo "CSI1/ISP0: ${CSI1_VIDEO}"
echo "CSI2/ISP1: ${CSI2_VIDEO}"
```

The helper also supports querying one interface directly:

```shell
isp-camera-devices --csi1
isp-camera-devices --csi2
```

Generate a single-camera GStreamer pipeline without running it with:

```shell
isp-camera-devices --gst-single csi1
isp-camera-devices --gst-single csi2
```

Run the generated single-camera pipeline directly with:

```shell
isp-camera-devices --run-single csi1
isp-camera-devices --run-single csi2
```

Generate or run the dual-camera side-by-side pipeline with:

```shell
isp-camera-devices --gst-dual
isp-camera-devices --run-dual
```

The generated pipelines default to 1920x1080 NV12 capture at 30 FPS and a
1024x600 Wayland output. Override individual settings when needed:

```shell
ISP_CAPTURE_WIDTH=1280 \
ISP_CAPTURE_HEIGHT=720 \
ISP_CAPTURE_FPS=30 \
ISP_DISPLAY_WIDTH=1024 \
ISP_DISPLAY_HEIGHT=600 \
isp-camera-devices --run-dual
```

The available variables are `ISP_CAPTURE_WIDTH`, `ISP_CAPTURE_HEIGHT`,
`ISP_CAPTURE_FPS`, `ISP_VIDEO_FORMAT`, `ISP_DISPLAY_WIDTH`,
`ISP_DISPLAY_HEIGHT`, and `ISP_GST_SINK`.

## Disabling the integrations

To exclude all integrations in this directory from a build, add the
following setting to `conf/local.conf`:

```bitbake
BBMASK += "meta-bsp-imx8mp/recipes-isp/"
```
