SUMMARY = "CompuLab i.MX8MP family bootloader bundle"
DESCRIPTION = "Build U-Boot boot containers for every supported CompuLab i.MX8MP machine and DRAM configuration"

require recipes-bsp/u-boot/u-boot.inc

# This is a family firmware payload, not the bootloader used to assemble the
# current machine's image.
PROVIDES:remove = "virtual/bootloader"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

UBOOT_VERSION = "2023.04"
SRCBRANCH = "u-boot-compulab_v2023.04-d1d8_d2d4"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
SRCREV = "${AUTOREV}"
# A '+' in PV enables Yocto's automatic SCM suffix in PKGV. SRCPV is
# deprecated and empty in this release; package.bbclass adds the resolved
# revision and replaces AUTOINC with the PR-service counter (0 without one).
# package_deb uses PKGV-PKGR in both the .deb filename and its Version field,
# for example: compulab-bootloader_2023.04+git0+<sha10>-r0_<arch>.deb.
PV = "${UBOOT_VERSION}+git"

DEPENDS += "flex-native bison-native bc-native dtc-native xz-native python3-setuptools-native u-boot-tools-native"

B = "${WORKDIR}/build"

# The standard U-Boot configuration type describes the current MACHINE and is
# not used by this family matrix. Clearing it also prevents inherited signing
# deploy hooks from looking for the standard <defconfig>-<type> build folder.
UBOOT_CONFIG = ""
UBOOT_MACHINE = "compulab-family"

COMPULAB_BOOTLOADER_FAMILY_MEMBERS = "${COMPULAB_IMX8MP_IOT_MACHINES} ${COMPULAB_IMX8MP_EVK_MACHINES}"
COMPULAB_BOOTLOADER_MACHINES ?= "${COMPULAB_BOOTLOADER_FAMILY_MEMBERS}"
COMPULAB_BOOTLOADER_DRAM_CONFIGS ?= "d1d8 d2d4"
COMPULAB_BOOTLOADER_DEFAULT_DRAM ?= "d2d4"
COMPULAB_BOOTLOADER_INSTALL_DIR = "/boot/${PN}"

inherit fsl-u-boot-localversion

# CONFIG_LOCALVERSION carries the release and DRAM configuration. Keep the SCM
# suffix, but do not add the class's default +fslc marker as well.
LOCALVERSION = ""

python __anonymous() {
    supported_machines = set((d.getVar("COMPULAB_BOOTLOADER_FAMILY_MEMBERS") or "").split())
    requested_machines = (d.getVar("COMPULAB_BOOTLOADER_MACHINES") or "").split()
    requested_dram = (d.getVar("COMPULAB_BOOTLOADER_DRAM_CONFIGS") or "").split()
    default_dram = d.getVar("COMPULAB_BOOTLOADER_DEFAULT_DRAM")

    if not requested_machines:
        bb.fatal("COMPULAB_BOOTLOADER_MACHINES must not be empty")

    unknown_machines = sorted(set(requested_machines) - supported_machines)
    if unknown_machines:
        bb.fatal("Unsupported CompuLab bootloader machines: %s" %
                 " ".join(unknown_machines))

    supported_dram = {"d1d8", "d2d4"}
    if not requested_dram or set(requested_dram) - supported_dram:
        bb.fatal("COMPULAB_BOOTLOADER_DRAM_CONFIGS must contain only d1d8 and/or d2d4")

    if default_dram not in requested_dram:
        bb.fatal("COMPULAB_BOOTLOADER_DEFAULT_DRAM must be present in COMPULAB_BOOTLOADER_DRAM_CONFIGS")
}

# The build consumes deployed firmware files rather than recipe sysroots.
do_configure[depends] += " \
    ${@' '.join('%s:do_deploy' % recipe for recipe in (d.getVar('IMX_EXTRA_FIRMWARE') or '').split())} \
    ${IMX_DEFAULT_ATF_PROVIDER}:do_deploy \
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'optee-os:do_deploy', '', d)} \
"

ATF_MACHINE_NAME ?= "bl31-${ATF_PLATFORM}.bin"
ATF_MACHINE_NAME:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', '-optee', '', d)}"

# These inherited tasks do not produce anything for this firmware bundle.
do_uboot_generate_rsa_keys[noexec] = "1"
do_uboot_assemble_fitimage[noexec] = "1"
do_create_extlinux_config[noexec] = "1"

compulab_prepare_build_dir() {
    build_dir="$1"

    install -d "${build_dir}"

    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        if [ ! -s "${DEPLOY_DIR_IMAGE}/${ddr_firmware}" ]; then
            bbfatal "Missing DDR firmware: ${DEPLOY_DIR_IMAGE}/${ddr_firmware}"
        fi
        ln -snf "${DEPLOY_DIR_IMAGE}/${ddr_firmware}" "${build_dir}/${ddr_firmware}"
    done

    if [ ! -s "${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}" ]; then
        bbfatal "Missing Arm Trusted Firmware: ${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}"
    fi
    ln -snf "${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}" "${build_dir}/bl31.bin"

    if ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}; then
        if [ ! -s "${DEPLOY_DIR_IMAGE}/tee.bin" ]; then
            bbfatal "Missing OP-TEE firmware: ${DEPLOY_DIR_IMAGE}/tee.bin"
        fi
        ln -snf "${DEPLOY_DIR_IMAGE}/tee.bin" "${build_dir}/tee.bin"
    fi
}

do_configure() {
    for machine in ${COMPULAB_BOOTLOADER_MACHINES}; do
        for dram_config in ${COMPULAB_BOOTLOADER_DRAM_CONFIGS}; do
            compulab_prepare_build_dir "${B}/${machine}-${dram_config}"
        done
    done
}

compulab_compile_config() {
    machine="$1"
    dram_config="$2"
    build_dir="${B}/${machine}-${dram_config}"

    oe_runmake -C "${S}" O="${build_dir}" \
        "${machine}_defconfig" "${dram_config}.config"

    sed -i '/^CONFIG_LOCALVERSION=/d' "${build_dir}/.config"
    printf '%s\n' \
        "CONFIG_LOCALVERSION=\"-${CL_RELEASE}-${dram_config}\"" \
        >> "${build_dir}/.config"
    oe_runmake -C "${S}" O="${build_dir}" olddefconfig

    # flash.bin-with-env depends on and leaves behind flash.bin and
    # u-boot-initial-env, so one make invocation produces all three outputs.
    oe_runmake -C "${S}" O="${build_dir}" flash.bin-with-env

    for output in flash.bin flash.bin-with-env u-boot-initial-env; do
        if [ ! -s "${build_dir}/${output}" ]; then
            bbfatal "Missing ${machine}/${dram_config} output: ${output}"
        fi
    done
}

do_compile() {
    unset LDFLAGS
    unset CFLAGS
    unset CPPFLAGS

    for machine in ${COMPULAB_BOOTLOADER_MACHINES}; do
        for dram_config in ${COMPULAB_BOOTLOADER_DRAM_CONFIGS}; do
            compulab_compile_config "${machine}" "${dram_config}"
        done
    done
}

compulab_deploy_machine() {
    machine="$1"
    compressed_dir="${DEPLOYDIR}/${PN}/${machine}"
    manufacturing_dir="${DEPLOYDIR}/${PN}/mfg/${machine}"

    install -d "${compressed_dir}" "${manufacturing_dir}"

    for dram_config in ${COMPULAB_BOOTLOADER_DRAM_CONFIGS}; do
        build_dir="${B}/${machine}-${dram_config}"

        xz -9c "${build_dir}/flash.bin" \
            > "${compressed_dir}/flash.bin.${dram_config}.xz"
        install -m 0644 "${build_dir}/flash.bin" \
            "${manufacturing_dir}/imx-boot_${machine}_${dram_config}"
        install -m 0644 "${build_dir}/flash.bin-with-env" \
            "${manufacturing_dir}/imx-boot_with-env_${machine}_${dram_config}"
    done

    install -m 0644 \
        "${B}/${machine}-${COMPULAB_BOOTLOADER_DEFAULT_DRAM}/u-boot-initial-env" \
        "${manufacturing_dir}/u-boot-initial-env-${machine}"
}

compulab_deploy_firmware() {
    firmware_dir="${DEPLOYDIR}/${PN}/imx8mp/firmware"
    install -d "${firmware_dir}"

    for ddr_firmware in ${DDR_FIRMWARE_NAME}; do
        gzip -9n -c "${DEPLOY_DIR_IMAGE}/${ddr_firmware}" \
            > "${firmware_dir}/${ddr_firmware}.gz"
    done

    gzip -9n -c "${DEPLOY_DIR_IMAGE}/${ATF_MACHINE_NAME}" \
        > "${firmware_dir}/bl31.bin.gz"

    if ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'true', 'false', d)}; then
        gzip -9n -c "${DEPLOY_DIR_IMAGE}/tee.bin" \
            > "${firmware_dir}/tee.bin.gz"
    fi
}

do_deploy() {
    for machine in ${COMPULAB_BOOTLOADER_MACHINES}; do
        compulab_deploy_machine "${machine}"
    done

    compulab_deploy_firmware
}

compulab_install_machine() {
    machine="$1"
    install_dir="${D}${COMPULAB_BOOTLOADER_INSTALL_DIR}"

    for dram_config in ${COMPULAB_BOOTLOADER_DRAM_CONFIGS}; do
        build_dir="${B}/${machine}-${dram_config}"
        install -m 0644 "${build_dir}/flash.bin" \
            "${install_dir}/imx-boot_${machine}_${dram_config}"
        install -m 0644 "${build_dir}/flash.bin-with-env" \
            "${install_dir}/imx-boot_with-env_${machine}_${dram_config}"
    done

    install -m 0644 \
        "${B}/${machine}-${COMPULAB_BOOTLOADER_DEFAULT_DRAM}/u-boot-initial-env" \
        "${D}${sysconfdir}/u-boot-initial-env-${machine}"
}

do_install() {
    install -d "${D}${COMPULAB_BOOTLOADER_INSTALL_DIR}" "${D}${sysconfdir}"

    for machine in ${COMPULAB_BOOTLOADER_MACHINES}; do
        compulab_install_machine "${machine}"
    done

    install -m 0644 "${S}/tools/env/fw_env.config" \
        "${D}${sysconfdir}/fw_env.config"

    (
        cd "${D}${COMPULAB_BOOTLOADER_INSTALL_DIR}"
        sha256sum imx-boot_* > SHA256SUMS
    )
}

FILES:${PN} = "${COMPULAB_BOOTLOADER_INSTALL_DIR}"
FILES:${PN}-env = " \
    ${sysconfdir}/u-boot-initial-env-* \
    ${sysconfdir}/fw_env.config \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(compulab-mx8mp)"

EXTRA_OEMAKE += "debug=n DEBUG=0"

# The updater needs the boot containers, not the optional environment package.
RDEPENDS:${PN}:remove = "${PN}-env"
