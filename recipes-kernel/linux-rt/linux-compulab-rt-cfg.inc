FILESEXTRAPATHS:prepend := "${THISDIR}/compulab/${PV}:"

SRC_URI:append = "${@bb.utils.contains('DISTRO_FEATURES', 'linux-rt', 'file://rt.cfg', '', d)}"
