# Optional generic-family bootloader update

> **Warning:** `compulab-bootloader` and `cl-firmware` are experimental and
> are not recommended for general or production use yet. They are optional and
> are not installed in images by default.

The packages implement an automatic bootloader update mechanism for the
generic `compulab-imx8mp` machine:

- The bundle uses U-Boot 2026.04 from `u-boot-compulab_v2026.04-imx8mp`.
  Each build selects `compulab-imx8mp_defconfig` and its target `.config`,
  producing one all-capacity image per machine.
- `compulab-bootloader` builds and packages boot containers for the complete
  eight supported CompuLab i.MX8MP hardware targets, including MCM-iMX8Pv2. It is a firmware payload package, not the
  `virtual/bootloader` provider used to create the initial image.
- `cl-firmware` installs and enables a oneshot systemd service. The service
  reads the product name from the board EEPROM, maps it to
  the corresponding packaged boot container, and compares that container with
  the bootloader in `/dev/mmcblk2boot0`.
- When the images differ, the service writes the selected bootloader to eMMC
  boot0, verifies it by reading it back, and requests an immediate reboot after
  a successful update. On later boots, a matching image is left unchanged.

The device must therefore start with a working bootloader already installed in
eMMC boot0. This mechanism runs later from Linux and must not be treated as a
replacement for the normal manufacturing or recovery procedure.

## Optional enablement

To evaluate the mechanism with `MACHINE = "compulab-imx8mp"`, explicitly add
both packages to the image from `conf/local.conf`:

```bitbake
CORE_IMAGE_EXTRA_INSTALL:append = " compulab-bootloader cl-firmware"
```

By default the recipe builds the complete family matrix; a machine can set
`COMPULAB_BOOTLOADER_MACHINES` to select a subset. Each target uses a separate
U-Boot build directory. Installed images are named `imx-boot_<machine>` and
`imx-boot_with-env_<machine>`, without a DRAM suffix. The payload includes
`/boot/compulab-bootloader/SHA256SUMS` for integrity checks.

Enabling `cl-firmware` authorizes an automatic write to eMMC boot0 during
system startup. Keep a tested recovery path available while evaluating it.

## Host validation

The filename-selection test mocks EEPROM output and does not access eMMC
or invoke the update/reboot functions:

```sh
python3 scripts/test-cl-firmware-selection.py
```
