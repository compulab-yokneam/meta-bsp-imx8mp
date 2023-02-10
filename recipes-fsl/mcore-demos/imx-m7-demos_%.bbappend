do_install:append () {
    install -d ${D}/boot/
    install -m 0644 ${S}/*.bin ${D}/boot/
    # Remove sai files, till it gets supported
    rm ${D}/boot/*sai*
    rm ${D}/lib/firmware/*sai*
}

FILES:${PN} += "/boot"
