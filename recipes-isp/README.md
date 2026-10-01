# Optional ISP support

This directory contains optional camera and ISP integrations for the NXP
i.MX8MP ISP stack.

## Supported sensors

| Sensor | VVCAM module | ISP configuration | Device-tree overlay |
| --- | --- | --- | --- |
| Sony IMX219 | `imx219` | `imx219_1080p30` | `sbev-ucmimx8plus-csi1-isp0-imx219.dtbo` |
| OmniVision OV5647 | `ov5647` | `ov5647_1080p30` | `sbev-ucmimx8plus-csi1-isp0-ov5647.dtbo` |

The `kernel-module-isp-vvcam` bbappend provides the sensor kernel modules.
The `isp-imx` bbappend provides the corresponding ISP drivers, calibration
data, runtime configurations, and automatic device-tree detection.

The overlays are built and deployed for the `ucm-imx8m-plus-sbev` machine.
Select the overlay for the connected sensor in the boot configuration. Do not
enable both overlays on the same CSI1/ISP0 pipeline at the same time.

## Disabling the integrations

To exclude all integrations in this directory from a build, add the
following setting to `conf/local.conf`:

```bitbake
BBMASK += "meta-bsp-imx8mp/recipes-isp/"
```
