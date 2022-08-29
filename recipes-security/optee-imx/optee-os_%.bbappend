FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
	file://0001-attestation-test-Avoid-asserts.patch \
"

OPTEE_CORE_LOG_LEVEL = "4"
OPTEE_TA_LOG_LEVEL = "4"

# EXTRA_OEMAKE:append = " CFG_DDR_SIZE=0x200000000ULL "
