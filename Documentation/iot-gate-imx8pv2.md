# IOT-GATE-IMX8PV2

Use `MACHINE=iot-gate-imx8pv2` for the CompuLab IOT-GATE-IMX8PV2
industrial IoT gateway based on the NXP i.MX8M Plus. Follow the
[build setup instructions](../README.md#configuring-the-build), selecting:

```sh
export MACHINE=iot-gate-imx8pv2
source compulab-setup-env build-${MACHINE}
```

Set `DRAM_CONF` in `conf/local.conf` to the board's DRAM subset, either
`d1d8` or `d2d4`. The default is `d2d4`.

The machine selects `iot-gate-imx8pv2_defconfig` for U-Boot and
`iot-gate-imx8pv2.dtb` as its U-Boot device tree. It uses the family U-Boot
branch `u-boot-compulab_v2023.04-d1d8_d2d4` and the kernel branch
`linux-compulab_v6.18.20`.

Build the image or bootloader with:

```sh
bitbake imx-image-full
bitbake u-boot-compulab
```

## Kernel payload

The shared inventory in
`conf/machine/include/machine-payload/iot-gate-imx8pv2.inc` lists the
machine's 16 device-tree artifacts. The default DTB,
`compulab/iot-gate-imx8pv2.dtb`, is listed first. The payload also includes
`compulab/iot-gate-imx8pv2-headless.dtb` and overlays for CAN A/B, the
command button, digital outputs 0–3, HDMI, modem, NVMe, RS485 A/B, Wi-Fi
and SDIO Wi-Fi. Including an overlay in the image does not apply it
automatically.

The machine adds `gpsd`, `gps-utils`, `ppp` and `modemmanager` through
`CORE_IMAGE_EXTRA_INSTALL`. A machine-specific configuration package is
not available yet and is not installed.

## Grouped images and family bootloaders

IOT-GATE-IMX8PV2 belongs to the `SUPER_IOT` payload group. Add this to
`conf/local.conf` to include the IOT-GATE-IMX8PLUS, IOT-GATE-IMX8PV2 and
IOTDIN payloads:

```bitbake
SUPER_IOT = "1"
```

The group can be enabled for any CompuLab i.MX8MP machine. The selected
machine continues to control the bootloader and default device tree.

Both `d1d8` and `d2d4` variants are included in the family bootloader
matrix. Re-enter the build through `compulab-setup-env` after updating the
layer so the new multiconfig templates are installed. See
[the boot firmware guide](imx_boot_image_build.md#build-the-complete-compulab-imx8mp-family)
for the `u-boot-compulab-multi` workflow and artifact paths.
