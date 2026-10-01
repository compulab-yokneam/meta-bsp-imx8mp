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
Select the overlay for the connected sensor in the boot configuration. Do not
enable more than one sensor overlay at the same time.

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

Reboot the system after changing `fdtofile`. Setting the variable replaces the
previous sensor overlay selection.

## Disabling the integrations

To exclude all integrations in this directory from a build, add the
following setting to `conf/local.conf`:

```bitbake
BBMASK += "meta-bsp-imx8mp/recipes-isp/"
```
