FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = "${@bb.utils.contains('PV', '1.17.1', ' file://0001-Fix-build-issue.patch ', '', d)}"
