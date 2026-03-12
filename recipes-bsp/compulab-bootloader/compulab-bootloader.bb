DESCRIPTION = "CLab i.MX8 U-Boot"
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += " ${PN} "
PROVIDES += " u-boot-initial-env "
PROVIDES += " u-boot-compulab-env "
PROVIDES:remove = "virtual/bootloader"

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
COMPULAB_BOOTLOADER_FAMILY_MEMBERS = "iot-gate-imx8plus iotdin-imx8p mcm-imx8m-plus som-imx8m-plus ucm-imx8m-plus ucm-imx8m-plus-sbev"
COMPULAB_BOOTLOADER_MACHINE = "${@bb.utils.contains('MACHINE', 'compulab-imx8mp', 'ucm-imx8m-plus-sbev', '${MACHINE}', d)}"
COMPULAB_BOOTLOADER_MACHINES ?= "${@bb.utils.contains('COMPULAB_BOOTLOADER_FAMILY', '1', '${COMPULAB_BOOTLOADER_FAMILY_MEMBERS}', '${COMPULAB_BOOTLOADER_MACHINE}', d)}"

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
    for MACH in ${COMPULAB_BOOTLOADER_MACHINES};do
        BOOTLOADER_CONFIG=${MACH}_defconfig do_configure1
    done
}

do_compile_dram_cfg() {
    DRAM_CFG=${1}

    oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} ${BOOTLOADER_CONFIG} ${DRAM_CFG}.config spl_size.config

    sed -i "/CONFIG_LOCALVERSION=/d" ${B}/${BOOTLOADER_CONFIG}/.config
    sed -i "$ a CONFIG_LOCALVERSION=\"${UBOOT_VERSION_EXTENSION}-${DRAM_CFG}\"" ${B}/${BOOTLOADER_CONFIG}/.config

    oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} flash.bin
    mv ${B}/${BOOTLOADER_CONFIG}/flash.bin ${B}/${BOOTLOADER_CONFIG}/flash.bin_${DRAM_CFG}

    oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} flash.bin-with-env
    mv ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_${DRAM_CFG}
}

do_compile1() {
    for dram_cfg in d2 d4 d1d8;do
        do_compile_dram_cfg ${dram_cfg}
    done

    oe_runmake -C ${S} O=${B}/${BOOTLOADER_CONFIG} u-boot-initial-env
    mv ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH}
}

do_compile() {
    for MACH in ${COMPULAB_BOOTLOADER_MACHINES};do
        BOOTLOADER_CONFIG=${MACH}_defconfig do_compile1
    done
}

do_deploy2() {
    D2=${DEPLOYDIR}/${PN}/${MACH}
    install -d ${D2}
    for dram_cfg in d2 d4 d1d8;do
        xz -9c ${B}/${BOOTLOADER_CONFIG}/flash.bin_${dram_cfg} > ${D2}/flash.bin.${dram_cfg}.xz
    done
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
    for dram_cfg in d2 d4 d1d8;do
        install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_${dram_cfg}  ${D1}/imx-boot_${MACH}_${dram_cfg}
        install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_${dram_cfg}  ${D1}/imx-boot_with-env_${MACH}_${dram_cfg}
    done
    install -m 0644 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH}  ${D1}/u-boot-initial-env-${MACH}
}

do_deploy0() {
    D0=${DEPLOYDIR}
    install -d ${D0}
    install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_${dram_cfg}  ${D0}/imx-boot_${MACH}_${dram_cfg}
    install -m 0644 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH}  ${D0}/u-boot-initial-env-${MACH}

    cp ${D0}/imx-boot_${MACH}_${dram_cfg} ${D0}/imx-boot.tagged
    stat -L -cUUUBURNXXOEUZX7+A-XY5601QQWWZ%sEND ${D0}/imx-boot.tagged >> ${D0}/imx-boot.tagged

    ln -fs imx-boot_${MACH}_${dram_cfg} ${D0}/imx-boot
    ln -fs u-boot-initial-env-${MACH} ${D0}/u-boot-initial-env
    ln -fs u-boot-initial-env-${MACH} ${D0}/u-boot-compulab-env
}

do_deploy() {
    # BOOTLOADER_CONFIG=${COMPULAB_BOOTLOADER_MACHINE}_defconfig MACH=${COMPULAB_BOOTLOADER_MACHINE} dram_cfg=${DRAM_CONF} do_deploy0
    for MACH in ${COMPULAB_BOOTLOADER_MACHINES};do
        BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy1
        BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy2
        BOOTLOADER_CONFIG=${MACH}_defconfig do_deploy3
    done
}

do_install1() {
    I1=${D}/boot/${PN}
    install -d ${I1}
    for dram_cfg in d2 d4 d1d8;do
        install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin_${dram_cfg} ${I1}/imx-boot_${MACH}_${dram_cfg}
        install -m 0644 ${B}/${BOOTLOADER_CONFIG}/flash.bin-with-env_${dram_cfg}  ${I1}/imx-boot_with-env_${MACH}_${dram_cfg}
    done
    install -d ${D}/etc
    install -m 0644 ${B}/${BOOTLOADER_CONFIG}/u-boot-initial-env-${MACH} ${D}/etc/u-boot-initial-env-${MACH}
    install -m 0644 ${S}/tools/env/fw_env.config  ${D}/etc/fw_env.config
}

do_install0() {
    I0=${D}/boot
    install -d ${I0}
    ln -s ${PN}/imx-boot_${MACH}_${dram_cfg} ${I0}/imx-boot_${MACH}_${dram_cfg}-flash_evk
    ln -s imx-boot_${MACH}_${dram_cfg}-flash_evk ${I0}/imx-boot
    ln -s u-boot-initial-env-${MACH} ${D}/etc/u-boot-initial-env
}

do_install() {
    for MACH in ${COMPULAB_BOOTLOADER_MACHINES};do
        MACH=${MACH} BOOTLOADER_CONFIG=${MACH}_defconfig do_install1
    done
    # BOOTLOADER_CONFIG=${COMPULAB_BOOTLOADER_MACHINE}_defconfig MACH=${COMPULAB_BOOTLOADER_MACHINE} dram_cfg=${DRAM_CONF} do_install0
}

FILES:${PN} = " \
    /boot \
"

FILES:${PN}-env = " \
    /etc/ \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE += "debug=n  DEBUG=0 "

# RREPLACES:${PN} = "imx-boot"
# RREPLACES:${PN}-env = "u-boot-compulab-env"

RDEPENDS:${PN}:remove = "${PN}-env"
