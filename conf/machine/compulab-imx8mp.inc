MACHINEOVERRIDES =. "imx-boot-container:mx8:mx8m:mx8mp:compulab-mx8mp:"

require conf/machine/include/imx-base.inc
require conf/machine/include/arm/armv8a/tune-cortexa53.inc

DEFAULTTUNE:use-mainline-bsp = "cortexa53-crypto"
DEFAULTTUNE:use-nxp-bsp = "cortexa53-crypto"

MACHINE_FEATURES += " pci wifi bluetooth "
MACHINE_FEATURES:append:use-nxp-bsp = " optee "

IMX_DEFAULT_BOOTLOADER:use-mainline-bsp = "u-boot-compulab"
IMX_DEFAULT_BOOTLOADER:use-nxp-bsp = "u-boot-compulab"

SPL_BINARY = "spl/u-boot-spl.bin"

ATF_PLATFORM = "imx8mp"
ATF_LOAD_ADDR = "0x970000"

# Extra firmware package name, that is required to build boot container for fslc bsp
IMX_EXTRA_FIRMWARE = "firmware-imx-8m"

# Set DDR FIRMWARE
DDR_FIRMWARE_VERSION = "202006"
DDR_FIRMWARE_NAME = " \
	lpddr4_pmu_train_1d_dmem_${DDR_FIRMWARE_VERSION}.bin \
	lpddr4_pmu_train_1d_imem_${DDR_FIRMWARE_VERSION}.bin \
	lpddr4_pmu_train_2d_dmem_${DDR_FIRMWARE_VERSION}.bin \
	lpddr4_pmu_train_2d_imem_${DDR_FIRMWARE_VERSION}.bin \
"

# Set imx-mkimage boot target
IMXBOOT_TARGETS = "flash_evk"

IMX_BOOT_SOC_TARGET = "iMX8MP"

# Set Serial console
SERIAL_CONSOLES = "115200;ttymxc1"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = "all"
IMX_BOOT_SEEK = "32"

OPTEE_BIN_EXT = "8mp"
TEE_LOAD_ADDR = "0x56000000"

DRAM_CONF ?= "d2d4"

IMAGE_INSTALL:append = " \
	kernel-image \
	kernel-modules \
	kernel-devicetree \
"

PREFERRED_PROVIDER_virtual/kernel:compulab-mx8mp = "linux-compulab"
PREFERRED_PROVIDER_virtual/bootloader:compulab-mx8mp = "u-boot-compulab"
PREFERRED_PROVIDER_linux-imx-mfgtool:compulab-mx8mp = "linux-compulab-mfgtool"
PREFERRED_PROVIDER_linux-compulab-mfgtool:compulab-mx8mp = "linux-compulab-mfgtool"

IMXBOOT_TARGETS_BASENAME = "flash_evk"

PREFERRED_RROVIDER_u-boot-fw-utils = "libubootenv-bin"

MACHINE_FIRMWARE:remove = " linux-firmware-ath10k "

IMX_DEFAULT_BSP = "nxp"
