DESCRIPTION = "CLab i.MX8 U-Boot"
require recipes-bsp/u-boot/u-boot.inc

PROVIDES = "${PN}"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

UBOOT_VERSION = "2023.04"
SRCBRANCH = "u-boot-compulab_v${UBOOT_VERSION}"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
PV = "${UBOOT_VERSION}+git${SRCPV}"
SRCREV = "${AUTOREV}"

DEPENDS += " flex-native bison-native bc-native dtc-native xz-native python3-setuptools-native u-boot-tools-native"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

UBOOT_VERSION_EXTENSION = "-${CL_RELEASE}"
COMPULAB_BOOTLOADER_MACHINE ?= "iot-gate-imx8plus iotdin-imx8p mcm-imx8m-plus som-imx8m-plus ucm-imx8m-plus ucm-imx8m-plus-sbev"

inherit fsl-u-boot-localversion

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
	sed -i '/CONFIG_SPL_MAX_SIZE/d' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '$ a CONFIG_SPL_MAX_SIZE=0x2C000' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '/CONFIG_SPL_PAD_TO/d' ${B}/${BOOTLOADER_CONFIG}/.config
	sed -i '$ a CONFIG_SPL_PAD_TO=0x2C000' ${B}/${BOOTLOADER_CONFIG}/.config
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

do_deploy2() {
	D2=${DEPLOYDIR}/${PN}/${MACH}
	install -d ${D2}
	xz -9c ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4 > ${D2}/flash.bin.d2d4.xz
	xz -9c ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8 > ${D2}/flash.bin.d1d8.xz
}

do_deploy3() {
	D3=${DEPLOYDIR}/${PN}/imx8mp/firmware
	if [ -d "${D3}" ];then
		return
	fi
	install -d ${D3}
	cp -L ${B}/${BOOTLOADER_CONFIG}/lpddr4_pmu_train_* ${D3}/
	cp -L ${B}/${BOOTLOADER_CONFIG}/bl31.bin ${D3}/
	cp -L ${B}/${BOOTLOADER_CONFIG}/tee.bin ${D3}/
	cd ${D3}
	for _bin in *.bin;do
		gzip -9 ${_bin}
	done
	cd -
}

do_deploy1() {
	D1=${DEPLOYDIR}/${PN}/mfg/${MACH}
	install -d ${D1}/
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4  ${D1}/imx-boot_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8  ${D1}/imx-boot_${MACH}_d1d8
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d2d4  ${D1}/imx-boot_with-env_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d1d8  ${D1}/imx-boot_with-env_${MACH}_d1d8
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH}  ${D1}/u-boot-initial-env-${MACH}
}

do_deploy() {
	for MACH in ${COMPULAB_BOOTLOADER_MACHINE};do
		BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy1
		BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy2
		BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy3
	done
}

do_install1() {
	I1=${D}/boot/${PN}
	install -d ${I1}
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d2d4 ${I1}/imx-boot_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_d1d8 ${I1}/imx-boot_${MACH}_d1d8
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d2d4  ${I1}/imx-boot_with-env_${MACH}_d2d4
	install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_d1d8  ${I1}/imx-boot_with-env_${MACH}_d1d8
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

RDEPENDS:${PN}:remove = "${PN}-env"
