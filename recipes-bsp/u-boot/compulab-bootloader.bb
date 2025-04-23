DESCRIPTION = "CLab i.MX8 U-Boot"
require recipes-bsp/u-boot/u-boot.inc

PROVIDES = "compulab-bootloader"
DEPENDS:append = " dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

UBOOT_VERSION = "2023.04"
SRCBRANCH = "u-boot-compulab_v${UBOOT_VERSION}"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
PV = "${UBOOT_VERSION}+git${SRCPV}"
SRCREV = "${AUTOREV}"

DEPENDS += "flex-native bison-native bc-native dtc-native"
DEPENDS += " python3-setuptools-native "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

UBOOT_VERSION_EXTENSION = "-${CL_RELEASE}"
COMPULAB_BOOTLOADER_MACHINE ?= "iot-gate-imx8plus iotdin-imx8p mcm-imx8m-plus som-imx8m-plus ucm-imx8m-plus ucm-imx8m-plus-sbev"

DEPENDS += " \
    ${IMX_EXTRA_FIRMWARE} \
    imx-atf \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os', '', d)} \
"

do_configure[depends] += " \
    ${@' '.join('%s:do_deploy' % r for r in '${IMX_EXTRA_FIRMWARE}'.split() )} \
    imx-atf:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

do_configure1() {
	mkdir -p ${B}/${BOOTLOADER_CONFIG}
	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} ${BOOTLOADER_CONFIG}
	for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
		ln -sf ${DEPLOY_DIR_IMAGE}/${ddr_firmware} ${B}/${BOOTLOADER_CONFIG}/
	done
	ln -sf ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME} ${B}/${BOOTLOADER_CONFIG}/bl31.bin
	ln -sf ${DEPLOY_DIR_IMAGE}/tee.bin ${B}/${BOOTLOADER_CONFIG}/
}

do_configure() {
	for MACH in ${COMPULAB_BOOTLOADER_MACHINE};do
		BOOTLOADER_CONFIG=${MACH}_defconfig do_configure1
	done
}

do_compile_d2d4() {
	sed -i '/CONFIG_DRAM_D2D4/d' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '$ a CONFIG_DRAM_D2D4=y' ${B}/${BOOTLOADER_CONFIG}/.config

	sed -i "/CONFIG_LOCALVERSION=/d" ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i "$ a CONFIG_LOCALVERSION=\"${UBOOT_VERSION_EXTENSION}-d2d4\"" ${B}/${BOOTLOADER_CONFIG}/.config

	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG}
	mv ${B}/${BOOTLOADER_CONFIG}/flash.bin ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4

	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} flash.bin-with-env
	mv ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d2d4
}

do_compile_d1d8() {
	sed -i '/CONFIG_DRAM_D2D4/d' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '$ a # CONFIG_DRAM_D2D4 is not set' ${B}/${BOOTLOADER_CONFIG}/.config

	sed -i "/CONFIG_LOCALVERSION=/d" ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i "$ a CONFIG_LOCALVERSION=\"${UBOOT_VERSION_EXTENSION}-d1d8\"" ${B}/${BOOTLOADER_CONFIG}/.config

	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG}
	mv ${B}/${BOOTLOADER_CONFIG}/flash.bin ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8

	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} flash.bin-with-env
	mv ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d1d8
}

do_compile1() {
	do_compile_d2d4
	do_compile_d1d8
	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} u-boot-initial-env
	mv ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH}
}

do_compile() {
	for MACH in ${COMPULAB_BOOTLOADER_MACHINE};do
		BOOTLOADER_CONFIG=${MACH}_defconfig do_compile1
	done
}

do_deploy1() {
	install -d ${DEPLOYDIR}/
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4  ${DEPLOYDIR}/imx-boot_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8  ${DEPLOYDIR}/imx-boot_${MACH}_d1d8
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d2d4  ${DEPLOYDIR}/imx-boot_with-env_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d1d8  ${DEPLOYDIR}/imx-boot_with-env_${MACH}_d1d8
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH}  ${DEPLOYDIR}/u-boot-initial-env-${MACH}
	ln -sf imx-boot_${MACH}_${DRAM_CONF} ${DEPLOYDIR}/imx-boot-${MACH}
}

do_deploy() {
	for MACH in ${COMPULAB_BOOTLOADER_MACHINE};do
		BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy1
	done
}

do_install1() {
	install -d ${D}/boot
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4 ${D}/boot/imx-boot_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8 ${D}/boot/imx-boot_${MACH}_d1d8
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d2d4  ${D}/boot/imx-boot_with-env_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d1d8  ${D}/boot/imx-boot_with-env_${MACH}_d1d8
	install -d ${D}/etc
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH} ${D}/etc/u-boot-initial-env-${MACH}
	install -m 0644 ${S}/tools/env/fw_env.config  ${D}/etc/fw_env.config
}

do_install() {
	for MACH in ${COMPULAB_BOOTLOADER_MACHINE};do
		MACH=${MACH} BOOTLOADER_CONFIG=${MACH}_defconfig do_install1
	done
}

FILES:${PN} = " \
	/boot \
"

FILES:${PN}-env = " \
	/etc/ \
"

PACKAGE_ARCH = "${MACHINE_SOCARCH}"

EXTRA_OEMAKE += "debug=n  DEBUG=0 "

RREPLACES:${PN} = "imx-boot"
RREPLACES:${PN}-env = "u-boot-compulab-env"
