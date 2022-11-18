SUMMARY = "CompuLab Linux Kernel for compulab-imx8m-plus SOM"

inherit kernel-yocto kernel fsl-kernel-localversion fsl-vivante-kernel-driver-handler

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

S = "${WORKDIR}/git"

# Tell to kernel class that we would like to use our defconfig to configure the kernel.
# Otherwise, the --allnoconfig would be used per default which leads to mis-configured
# kernel.
#
# This behavior happens when a defconfig is provided, the kernel-yocto configuration
# uses the filename as a trigger to use a 'allnoconfig' baseline before merging
# the defconfig into the build.
#
# If the defconfig file was created with make_savedefconfig, not all options are
# specified, and should be restored with their defaults, not set to 'n'.
# To properly expand a defconfig like this, we need to specify: KCONFIG_MODE="--alldefconfig"
# in the kernel recipe include.
KCONFIG_MODE="--alldefconfig"

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

DEPENDS += "lzop-native bc-native"

require linux-compulab_${PV}.inc

DEFAULT_PREFERENCE = "1"

KERNEL_VERSION_SANITY_SKIP="1"

# Merged from the bbappend
FILESEXTRAPATHS:prepend := "${THISDIR}/compulab/${PV}/imx8mp:"

require compulab/${PV}/imx8mp.inc

do_configure:append() {
    if [ -z ${SKIP_MACHINE_DEFCONFIG} ];then
        if [ -z ${KERNEL_CONFIG} ];then
            oe_runmake ${MACHINE}_defconfig
        else
            oe_runmake ${KERNEL_CONFIG}
        fi
    fi
}

do_merge_config () {
    configs=arch/arm64/configs
    cp ${S}/${configs}/${MACHINE}_defconfig ${S}/${configs}/compulab_defconfig
    echo "# CONFIG_MACHINE_STUB is not set " > ${WORKDIR}/CONFIG_MACHINE_STUB.cfg
    ${S}/scripts/kconfig/merge_config.sh  -O ${S}/${configs}/ -m ${S}/${configs}/${MACHINE}_defconfig ${WORKDIR}/*.cfg
    mv ${S}/${configs}/.config ${S}/${configs}/${MACHINE}_defconfig
    oe_runmake ${MACHINE}_defconfig
    mv ${S}/${configs}/compulab_defconfig ${S}/${configs}/${MACHINE}_defconfig
}

do_compile:prepend() {
    if [ "${BUILD_REPRODUCIBLE_BINARIES}" = "1" ];then
        export SOURCE_DATE_EPOCH=$(date +%s)
    fi
    do_merge_config
}

do_install:append() {
    oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${KERNEL_VERSION} ARCH=$ARCH
}

do_kernel_localversion:prepend() {
    touch ${WORKDIR}/defconfig
}

PACKAGES =+ "linux-compulab-headers"
FILES:linux-compulab-headers = "${exec_prefix}/src/linux*"

PACKAGESPLITFUNCS:remove = "split_kernel_module_packages"

FILES:${KERNEL_PACKAGE_NAME}-modules = "/lib/modules/ /etc/"

COMPATIBLE_MACHINE = "(ucm-imx8m-plus|som-imx8m-plus|iot-gate-imx8plus)"
