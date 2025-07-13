#!/bin/bash

EEPROM_CHIP="1 0x50"
COMPULAB_BOOTLOADER_FOLDER="/boot/compulab-bootloader"
FW_MEDIA="/dev/mmcblk2boot0"
TMP_FW="/tmp/$(basename ${FW_MEDIA})"

declare -A clname_oename=()
declare -A clname_oename=()
clname_oename+=( ['iotdin-imx8p']='iotdin-imx8p' )
clname_oename+=( ['iotg-imx8plus']='iot-gate-imx8plus' )
clname_oename+=( ['mcm-imx8plus']='mcm-imx8m-plus' )
clname_oename+=( ['ucm-imx8plus']='ucm-imx8m-plus' )
clname_oename+=( ['som-imx8plus']='som-imx8m-plus' )

declare -A dram_map=()
dram_map+=( ['d1']='d1d8' )
dram_map+=( ['d2']='d2d4' )
dram_map+=( ['d4']='d2d4' )
dram_map+=( ['d8']='d1d8' )

function firmware_name() {

dev_name=$(eeprom-util read ${EEPROM_CHIP} 2>/dev/null | awk '(/Product Name/)&&($0=tolower($NF))')
dram_size=$(eeprom-util read ${EEPROM_CHIP} 2>/dev/null | awk '(/Product Options/)&&($0=tolower($NF))&&(gsub(/-/,"\n"))' | sed -n '/^d/p')

dev_name=${dev_name:-"bad"}
dram_size=${dram_size:-"bad"}

device_name=${clname_oename[${dev_name}]:-"empty_eeprom"}
device_dram_size=${dram_map[${dram_size}]:-"empty_eeprom"}

echo "cl-firmware: firmware_name imx-boot_${device_name}_${device_dram_size}" > /dev/kmsg
echo imx-boot_${device_name}_${device_dram_size}

}

nothing_to_do() {
	echo "cl-firmware: Already updated" > /dev/kmsg
	[ -e /boot/$(basename ${FW_FILE}) ] || ln -fs ${FW_FILE} /boot/
	rm -rf ${TMP_FW}
	exit 0
}

fast_boot() {
	for _c in s u b;do
		echo ${_c} > /proc/sysrq-trigger
	done
}

validate_firmware() {

FW_FILE=${COMPULAB_BOOTLOADER_FOLDER}/$(firmware_name)

echo "cl-firmware: validate_firmware ${FW_FILE}" > /dev/kmsg

if [ -f ${FW_FILE} ];then
	FW_SIZE=$(stat --format=%s ${FW_FILE})
	dd if=${FW_MEDIA} of=${TMP_FW} bs=${FW_SIZE} count=1 &>/dev/null
	diff ${FW_FILE} ${TMP_FW} &>/dev/null && nothing_to_do || {
		echo "cl-firmware: QUIET=Yes SRC=${FW_FILE} DST=${FW_MEDIA} cl-uboot.work" > /dev/kmsg
		QUIET=Yes SRC=${FW_FILE} DST=${FW_MEDIA} cl-uboot.work
		dd if=${FW_MEDIA} of=${TMP_FW} bs=${FW_SIZE} count=1 &>/dev/null
		diff ${FW_FILE} ${TMP_FW} &>/dev/null && fast_boot || true
		echo "cl-firmware: Update failed" > /dev/kmsg
	}
fi
}

validate_firmware
