FILES:${PN}-ax210 += " \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-59.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-62.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-63.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-66.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0-67.ucode \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0.pnvm \
	${nonarch_base_libdir}/firmware/iwlwifi-ty-a0-gf-a0.pnvm \
	${nonarch_base_libdir}/firmware/intel/ibt-0041-0041.sfi \
	${nonarch_base_libdir}/firmware/intel/ibt-0041-0041.ddc \
"

PACKAGES =+ " ${PN}-ax210 "
PROVIDES =+ " ${PN}-ax210 "
