DESCRIPTION = "CLab i.MX8 U-Boot"
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"
DEPENDS:append = " dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCBRANCH = "u-boot-compulab_v2022.04"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
PV = "1.0+git${SRCPV}"
SRCREV = "${AUTOREV}"

DEPENDS += "flex-native bison-native bc-native dtc-native"
DEPENDS += " python3-setuptools-native "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

LOCALVERSION = "-compulab"
UBOOT_CONFIG = "${MACHINE}_defconfig"

do_configure () {
	mkdir -p ${B}/${UBOOT_CONFIG}
	cd ${B}/${UBOOT_CONFIG}
	oe_runmake -C ${S} O=${B}/${UBOOT_CONFIG} ${UBOOT_CONFIG}
	oe_runmake -C ${S}/board/compulab/plat/imx8mp/firmware O=${B}/${UBOOT_CONFIG} all
}

do_compile() {
	cd ${B}/${UBOOT_CONFIG}
	oe_runmake -C ${S} O=${B}/${UBOOT_CONFIG} flash.bin
	oe_runmake -C ${S} O=${B}/${UBOOT_CONFIG} u-boot-initial-env
}

do_deploy () {
	install -d ${DEPLOYDIR}/
	install -m 0777 ${B}/${UBOOT_CONFIG}/flash.bin  ${DEPLOYDIR}/${UBOOT_NAME}
}

do_install () {
	install -d ${D}/boot
	install -m 0755 ${B}/${UBOOT_CONFIG}/flash.bin ${D}/boot/flash.bin
	install -d ${D}/etc
	install -m 0755 ${B}/${UBOOT_CONFIG}/u-boot-initial-env ${D}/etc/
	install -m 0644 ${S}/tools/env/fw_env.config  ${D}/etc/fw_env.config
}

FILES:${PN} = " \
	/boot \
"

FILES:${PN}-env = " \
	/etc/ \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
UBOOT_NAME = "flash-${MACHINE}.bin-${UBOOT_CONFIG}"
COMPATIBLE_MACHINE = "${MACHINE}-devel"
