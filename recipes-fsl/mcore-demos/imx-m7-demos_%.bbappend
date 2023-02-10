do_install:append () {
    install -d ${D}/boot/
    install -m 0644 ${S}/*.bin ${D}/boot/
}

FILES:${PN} += "/boot"
