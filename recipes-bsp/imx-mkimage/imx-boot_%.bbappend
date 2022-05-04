do_install:append () {
        ln -fs ${BOOT_CONFIG_MACHINE}-${target} ${D}/boot/imx-boot
}
