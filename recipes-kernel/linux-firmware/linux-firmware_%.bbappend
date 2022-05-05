FILES:${PN}-ax210 += " \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-59.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-62.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-63.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-66.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-67.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0.pnvm \
"

PACKAGES =+ " ${PN}-ax210 "
PROVIDES =+ " ${PN}-ax210 "
