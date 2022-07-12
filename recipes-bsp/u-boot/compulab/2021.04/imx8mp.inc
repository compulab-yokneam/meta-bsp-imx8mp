SRC_URI:append = " \
	file://0000-ucm-imx8m-plus-Add-support-update-files.patch \
	file://0001-ucm-imx8m-plus-Add-support-Add-files.patch \
	file://0002-ucm-imx8m-plus-Fix-spl-reset.patch \
	file://0003-ucm-imx8m-plus-spl-eeprom-Enable-DM-mode.patch \
	file://0004-ucm-imx8m-plus-dts-Update-u-boot-device-tree.patch \
	file://0005-ucm-imx8m-plus-spl-Update-the-machine-spl.patch \
	file://0006-ucm-imx8m-plus-config-Update-for-SR.patch \
	file://0007-ucm-imx8m-plus-config-Update-ENV-settings.patch \
	file://0008-ucm-imx8m-plus-dts-Add-tty0-as-a-console.patch \
	file://0009-ucm-imx8m-plus-env-Provide-the-CompuLab-bsp_bootcmd.patch \
	file://0010-ucm-imx8m-plus-conf-Update-boot-environment.patch \
	file://0011-ucm-imx8m-plus-conf-Enable-watchdog.patch \
	file://0012-ucm-imx8m-plus-Kconfig-Add-SYS_I2C_EEPROM_BUS-ADDR.patch \
	file://0013-plat-imx8mp-ddr-Remove-the-specific-SoM-limitation.patch \
	file://0014-compulab-imx8mp-Prepare-for-multi-board-support.patch \
	file://0015-compulab-Add-som-imx8m-plus-support.patch \
	file://0016-watchdog-Add-CONFIG_WATCHDOG_AUTOSTART-option.patch \
	file://0017-som-imx8m-plus-config-Disable-CONFIG_WATCHDOG_AUTOST.patch \
	file://0018-compulab-imx8mp-ddr-Fix-service-gpio-error.patch \
	file://0019-som-imx8m-plus-Update-configs.patch \
	file://0020-som-imx8m-plus-Update-dts.patch \
	file://0021-som-imx8m-plus-Init-usdhc1-with-power-down-pin-low.patch \
	file://0022-imx8m-Addresses-the-SDP-common-board_f.c-955-hang-is.patch \
	file://0023-ucm-imx8m-plus-config-Disable-CONFIG_WATCHDOG_AUTOST.patch \
	file://0024-ucm-imx8m-plus-config-Enable-CONFIG_USB_STORAGE.patch \
	file://0025-compulab-imx8m-plus-Move-NET-settings-to-the-machine.patch \
	file://0026-compulab-imx8m-plus-Add-boot_opt.patch \
	file://0027-som-imx8m-plus-Enable-FEC0-enet-clock.patch \
	file://0028-som-imx8m-plus-dts-Add-phy-reset-for-net-ifaces.patch \
	file://0029-som-imx8m-plus-Set-ethprime-to-eth0.patch \
	file://0030-compulab-eeprom-add-support-for-obtaining-2-nd-mac.patch \
	file://0031-imx8m-plus-Add-board-specific-imx_get_mac_from_fuse.patch \
	file://0032-compulab-imx8mp-Add-CONFIG_ATF_LOAD_ADDR-CONFIG_TEE_.patch \
	file://0033-spi-Add-w25128dw-configuration-string.patch \
	file://0034-som-imx8m-plus-dts-Fix-the-flexspi-flash0-name.patch \
	file://0035-compulab-imx8m-plus-defconfig-Enable-CONFIG_NXP_FSPI.patch \
	file://0037-compulab-imx8mp-dts-Increase-the-temperature-thresho.patch \
	file://0038-compulab-imx8m-plus-Update-bootlist-with-usb.patch \
	file://0039-imx8m-plus-Move-phy-reset-gpio-to-device-tree.patch \
	file://0040-som-imx8m-plus-Remove-excessive-phy-reset.patch \
	file://0041-imx8m-plus-Add-Ethernet-PHY-RTL8211E-VB-CG-support.patch \
	file://0042-imx8m-plus-Enable-CMD_MTEST.patch \
	file://0043-som-imx8m-plus-Update-the-CONFIG_SYS_PROMPT.patch \
	file://0044-ucm-imx8m-plus-Update-the-CONFIG_SYS_PROMPT.patch \
	file://0045-compulab-imx8mp-dts-Increase-the-soc-temperature-thr.patch \
	file://0046-cpu-Change-minc-maxc-default-values.patch \
	file://0047-imx8mp-defconfig-Enable-CONFIG_CMD_PMIC.patch \
	file://0048-compulab-eeprom-Add-ldo4-get-set-functions.patch \
	file://0049-imx8mp-spl-Add-ldo4-init-support.patch \
	file://0050-imx8mp-board-Add-ldo4-command.patch \
	file://0051-imx8mp-board-Add-dp_pmic_ldo4.patch \
	file://0052-imx8mp-spl-Improve-the-ldo4-set-value-logics.patch \
	file://0053-compulab-eeprom-Issue-net_random_ethaddr-if-eeprom-i.patch \
	file://0054-ucm-imx8m-plus-Issue-cl_eeprom_read_n_mac_addr-for-m.patch \
	file://0055-ucm-imx8m-plus-Allow-usieng-UART1-as-debug-console.patch \
	file://0056-compulab-Add-iot-gate-imx8plus-support.patch \
	file://0057-iot-gate-imx8plus-restore-default-network-interfaces.patch \
	file://0058-iot-gate-imx8plus-fix-temperature-limits.patch \
"

SRC_URI:append:ucm-imx8m-plus = " \
	file://ucm-imx8m-plus_defconfig \
"

SRC_URI:append:som-imx8m-plus = " \
	file://som-imx8m-plus_defconfig \
"

SRC_URI:append:iot-gate-imx8plus = " \
	file://iot-gate-imx8plus_defconfig \
"
