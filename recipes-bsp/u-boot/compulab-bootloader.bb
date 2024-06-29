DESCRIPTION = "CLab i.MX8 U-Boot"
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"
DEPENDS:append = " dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCBRANCH = "u-boot-compulab_v2023.04"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
PV = "1.0+git${SRCPV}"
SRCREV = "${AUTOREV}"

DEPENDS += "flex-native bison-native bc-native dtc-native"
DEPENDS += " python3-setuptools-native "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

UBOOT_VERSION_EXTENSION = "-${CL_RELEASE}"
BOOTLOADER_CONFIG = "${MACHINE}_defconfig"

do_configure () {
	mkdir -p ${B}/${BOOTLOADER_CONFIG}
	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} ${BOOTLOADER_CONFIG}
}

do_compile_d2d4() {
	sed -i '/CONFIG_DRAM_D2D4/d' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '$ a CONFIG_DRAM_D2D4=y' ${B}/${BOOTLOADER_CONFIG}/.config

	sed -i "/CONFIG_LOCALVERSION=/d" ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i "$ a CONFIG_LOCALVERSION=\"${UBOOT_VERSION_EXTENSION}-d2d4\"" ${B}/${BOOTLOADER_CONFIG}/.config

	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG}
	mv ${B}/${BOOTLOADER_CONFIG}/flash.bin ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4
}

do_compile_d1d8() {
	sed -i '/CONFIG_DRAM_D2D4/d' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '$ a # CONFIG_DRAM_D2D4 is not set' ${B}/${BOOTLOADER_CONFIG}/.config

	sed -i "/CONFIG_LOCALVERSION=/d" ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i "$ a CONFIG_LOCALVERSION=\"${UBOOT_VERSION_EXTENSION}-d1d8\"" ${B}/${BOOTLOADER_CONFIG}/.config

	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG}
	mv ${B}/${BOOTLOADER_CONFIG}/flash.bin ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8
}

do_compile() {
	do_compile_d2d4
	do_compile_d1d8
	oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} u-boot-initial-env
}

do_deploy () {
	install -d ${DEPLOYDIR}/
	install -m 0777 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4  ${DEPLOYDIR}/imx-boot_${MACHINE}_d2d4
	install -m 0777 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8  ${DEPLOYDIR}/imx-boot_${MACHINE}_d1d8
	ln -sf imx-boot_${MACHINE}_${DRAM_CONF} ${DEPLOYDIR}/imx-boot-${MACHINE}
}

do_install () {
	install -d ${D}/boot
	install -m 0755 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4 ${D}/boot/imx-boot_${MACHINE}_d2d4
	install -m 0755 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8 ${D}/boot/imx-boot_${MACHINE}_d1d8
	install -d ${D}/etc
	install -m 0755 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env ${D}/etc/
	install -m 0644 ${S}/tools/env/fw_env.config  ${D}/etc/fw_env.config
}

FILES:${PN} = " \
	/boot \
"

FILES:${PN}-env = " \
	/etc/ \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "${MACHINE}"

EXTRA_OEMAKE += "debug=n  DEBUG=0 "

