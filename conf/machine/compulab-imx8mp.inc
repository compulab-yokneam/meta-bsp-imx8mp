MACHINEOVERRIDES =. "mx8:mx8m:mx8mp:"

require conf/machine/include/imx-base.inc
require conf/machine/include/tune-cortexa53.inc

IMX_DEFAULT_BSP = "nxp"

MACHINE_FEATURES += " pci wifi bluetooth optee "

KERNEL_DEVICETREE = " \
	compulab/ucm-imx8m-plus.dtb \
"

UBOOT_CONFIG ??= "sd"
UBOOT_CONFIG[sd] = "ucm-imx8m-plus_defconfig,sdcard"
UBOOT_CONFIG[mfgtool] = "ucm-imx8m-plus_defconfig"
SPL_BINARY = "spl/u-boot-spl.bin"

FW_VER = "${@bb.utils.contains('DISTRO_CODENAME', 'dunfell' , '201904' , '202006' , d)}"
# Set DDR FIRMWARE
DDR_FIRMWARE_NAME = " \
	lpddr4_pmu_train_1d_dmem_${FW_VER}.bin \
	lpddr4_pmu_train_1d_imem_${FW_VER}.bin \
	lpddr4_pmu_train_2d_dmem_${FW_VER}.bin \
	lpddr4_pmu_train_2d_imem_${FW_VER}.bin \
"

# Set u-boot DTB
UBOOT_DTB_NAME = "ucm-imx8m-plus.dtb"

# Set imx-mkimage boot target
IMXBOOT_TARGETS = "flash_evk"

# Set Serial console
SERIAL_CONSOLES = "115200;ttymxc1"

IMAGE_BOOTLOADER = "imx-boot"

LOADADDR = ""
UBOOT_SUFFIX = "bin"
UBOOT_MAKE_TARGET = ""
IMX_BOOT_SEEK = "32"

OPTEE_BIN_EXT = "8mp"

IMAGE_INSTALL_append = " \
	kernel-image \
	kernel-modules \
	kernel-devicetree \
"
