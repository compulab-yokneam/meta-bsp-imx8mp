SUMMARY = "CompuLab i.MX8MP U-Boot boot container bundle"
DESCRIPTION = "Aggregate independently built machine and DRAM boot containers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy

INHIBIT_DEFAULT_DEPS = "1"

COMPULAB_UBOOT_MULTI_CONFIGS ??= ""
COMPULAB_UBOOT_MULTI_MATRIX ??= ""
COMPULAB_UBOOT_MULTI_MCDEPENDS = "${@' '.join('mc::%s:u-boot-compulab:do_deploy' % mc for mc in (d.getVar('COMPULAB_UBOOT_MULTI_CONFIGS') or '').split())}"

python __anonymous() {
    configs = (d.getVar("COMPULAB_UBOOT_MULTI_CONFIGS") or "").split()
    matrix = (d.getVar("COMPULAB_UBOOT_MULTI_MATRIX") or "").split()

    if not configs or not matrix:
        raise bb.parse.SkipRecipe(
            "u-boot-compulab-multi requires a CompuLab U-Boot "
            "multiconfig configuration")

    enabled = (d.getVar("BBMULTICONFIG") or "").split()
    missing = sorted(set(configs) - set(enabled))
    if missing:
        bb.fatal("Missing U-Boot multiconfigs: %s" % " ".join(missing))

    matrix_configs = []
    for entry in matrix:
        fields = entry.split(":")
        if len(fields) != 3 or not all(fields):
            bb.fatal("Invalid COMPULAB_UBOOT_MULTI_MATRIX entry: %s" % entry)
        matrix_configs.append(fields[0])

    if sorted(configs) != sorted(matrix_configs):
        bb.fatal("COMPULAB_UBOOT_MULTI_CONFIGS and "
                 "COMPULAB_UBOOT_MULTI_MATRIX do not match")
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install[mcdepends] = "${COMPULAB_UBOOT_MULTI_MCDEPENDS}"

do_install() {
    destination_root="${D}/boot/${PN}"
    install -d "${destination_root}"

    for entry in ${COMPULAB_UBOOT_MULTI_MATRIX}; do
        multiconfig="${entry%%:*}"
        remainder="${entry#*:}"
        machine="${remainder%%:*}"
        dram_conf="${remainder##*:}"
        source_dir="${TOPDIR}/tmp-${multiconfig}/deploy/images/${machine}"
        destination_dir="${destination_root}/${machine}"

        install -d "${destination_dir}"

        source_file="${source_dir}/flash.bin.${dram_conf}"
        if [ ! -s "${source_file}" ]; then
            bbfatal "Missing U-Boot image: ${source_file}"
        fi
        install -m 0644 "${source_file}" \
            "${destination_dir}/flash.bin-${dram_conf}"

        source_file="${source_dir}/flash.bin.tagged.${dram_conf}"
        if [ ! -s "${source_file}" ]; then
            bbfatal "Missing tagged U-Boot image: ${source_file}"
        fi
        install -m 0644 "${source_file}" \
            "${destination_dir}/flash.bin-${dram_conf}.tagged"
    done
}

do_deploy() {
    source_root="${D}/boot/${PN}"
    destination_root="${DEPLOYDIR}/${PN}"

    install -d "${destination_root}"
    for entry in ${COMPULAB_UBOOT_MULTI_MATRIX}; do
        remainder="${entry#*:}"
        machine="${remainder%%:*}"
        dram_conf="${remainder##*:}"
        source_dir="${source_root}/${machine}"
        destination_dir="${destination_root}/${machine}"

        install -d "${destination_dir}"
        install -m 0644 "${source_dir}/flash.bin-${dram_conf}" \
            "${destination_dir}/flash.bin-${dram_conf}"
        install -m 0644 "${source_dir}/flash.bin-${dram_conf}.tagged" \
            "${destination_dir}/flash.bin-${dram_conf}.tagged"
    done
}

addtask deploy after do_install before do_build

FILES:${PN} = "/boot/${PN}"
PACKAGE_ARCH = "all"

# PACKAGE_ARCH does not automatically extend the SPDX sstate search path.
SPDX_MULTILIB_SSTATE_ARCHS:append = " ${SSTATE_PKGARCH}"

COMPATIBLE_MACHINE = "(compulab-mx8mp)"
