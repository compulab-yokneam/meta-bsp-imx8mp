do_install_append () {
        ln -fs ${BOOT_CONFIG_MACHINE}-${target} ${D}/boot/imx-boot
}
