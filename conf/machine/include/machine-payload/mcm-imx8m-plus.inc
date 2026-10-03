COMPULAB_MCM_IMX8M_PLUS_DEVICETREES = " \
	compulab/mcm-imx8m-plus-som.dtb \
	compulab/mcm-imx8m-plus.dtb \
	compulab/mcm-imx8m-plus-evk-audio.dtb \
	compulab/mcm-imx8m-plus-evk-eth.dtb \
	compulab/mcm-imx8m-plus-headless.dtb \
	compulab/mcm-imx8m-plus-thermal.dtb \
	compulab/mcm-imx8m-plus-mipi.dtb \
	compulab/mcm-imx8m-plus-lvds.dtb \
	compulab/mcm-imx8m-plus-hdmi.dtb \
	compulab/mcm-imx8m-plus_mipi-csi1-ar1335-af.dtb \
	compulab/mcm-imx8m-plus_mipi-csi2-ar1335-af.dtb \
	compulab/mcm-imx8m-plus_mipi-csi1-ar1335-mcu.dtb \
	compulab/mcm-imx8m-plus_mipi-csi2-ar1335-mcu.dtb \
	compulab/mcm-imx8m-plus-eb-eth.dtbo \
	compulab/mcm-imx8m-plus-eb-audimx9.dtbo \
	compulab/mcm-imx8m-plus-pcie.dtbo \
	compulab/mcm-imx8m-plus-mipi.dtbo \
	compulab/mcm-imx8m-plus-lvds.dtbo \
	compulab/mcm-imx8m-plus-hdmi.dtbo \
	compulab/mcm-imx8m-plus-usbdev.dtbo \
	compulab/mcm-imx8m-plus-rpmsg.dtbo \
	compulab/mcm-imx8m-plus-ldo4.dtbo \
	compulab/mcm-imx8m-plus-spidev.dtbo \
	compulab/mcm-imx8m-plus-sd8997.dtbo \
	compulab/mcm-imx8m-plus-mmc2.dtbo \
	compulab/mcm-imx8m-plus-thermal.dtbo \
	compulab/mcm-imx8m-plus-uart1.dtbo \
	compulab/mcm-imx8m-plus-uart3.dtbo \
	compulab/mcm-imx8m-plus-uart4.dtbo \
	compulab/mcm-imx8m-plus_mipi-csi1-ar1335-af.dtbo \
	compulab/mcm-imx8m-plus_mipi-csi2-ar1335-af.dtbo \
	compulab/mcm-imx8m-plus_mipi-csi1-ar1335-mcu.dtbo \
	compulab/mcm-imx8m-plus_mipi-csi2-ar1335-mcu.dtbo \
"

COMPULAB_MCM_IMX8M_PLUS_MACHINE_FEATURES = "mrvl8997"

# The common CompuLab configuration installs imx-firmware-mrvl, including the
# SD8997 combo firmware and its sd8997_uapsta/sdsd8997 aliases. Do not install
# linux-firmware-sd8997 as it owns the same paths with different contents.
COMPULAB_MCM_IMX8M_PLUS_MACHINE_FIRMWARE = " \
	linux-firmware-iwlwifi-misc \
"
