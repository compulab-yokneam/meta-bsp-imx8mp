# Optional generic-family firmware updater

The generic `compulab-imx8mp` machine has an optional experimental firmware
update mechanism consisting of the `compulab-bootloader` and `cl-firmware`
packages. These packages are not installed by default and are not recommended
for general or production use yet.

The system initially boots from the bootloader already installed in eMMC
boot0. When the optional packages are enabled, the `cl-firmware` service reads
the product and DRAM configuration from EEPROM during Linux startup, selects
the corresponding family bootloader, and updates eMMC boot0 when its contents
differ. See
[`recipes-bsp/compulab-bootloader/README.md`](../recipes-bsp/compulab-bootloader/README.md)
before enabling this mechanism.
