# Linux 6.18 uses transitional Kconfig symbols, which the Scarthgap parser
# does not support. Backport the parser fix while retaining config checks.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-tools-Kconfiglib-add-support-for-transitional-attribut.patch"
