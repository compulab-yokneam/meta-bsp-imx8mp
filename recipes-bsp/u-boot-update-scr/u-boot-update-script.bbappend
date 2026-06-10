FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

do_compile:imx-boot-container() {
    BOOTLOADER=flash.bin-${MACHINE}-${DRAM_CONF}
    sed "s|@@BOOTLOADER@@|${BOOTLOADER}|;s|@@DRAM_CONF@@|${DRAM_CONF}|" ${WORKDIR}/boot.script > ${WORKDIR}/boot.update.in
    mkimage -C none -A arm -T script -d ${WORKDIR}/boot.update.in ${WORKDIR}/boot.update.scr
}

DEPENDS:remove = "imx-boot"
DEPENDS:append = " u-boot-compulab"
