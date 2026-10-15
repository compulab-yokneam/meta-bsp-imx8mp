# Scarthgap does not consume UBOOT_CONFIG_FRAGMENTS. Apply the fragments
# from the U-Boot source tree after configuring each selected defconfig.
do_configure:append() {
    for config in ${UBOOT_MACHINE}; do
        ${S}/scripts/kconfig/merge_config.sh -m -O ${B}/${config} \
            ${B}/${config}/.config \
            ${S}/configs/d0.config ${S}/configs/${DRAM_CONF}.config
        oe_runmake -C ${S} O=${B}/${config} olddefconfig
    done
}
