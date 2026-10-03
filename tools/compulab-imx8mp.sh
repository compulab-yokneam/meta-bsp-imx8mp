#!/bin/bash

MACHINE_FOLDER=${BUILDDIR}/../sources/meta-bsp-imx8mp/conf/machine
MACHINE_FAMILY="compulab-imx8mp.conf"
MACHINE_MAIN="ucm-imx8m-plus"

{
	echo 'SUPER_EVK = "1"'
	echo 'SUPER_IOT = "1"'
	echo
	echo "require ${MACHINE_MAIN}.conf"
	echo
	echo 'WKS_BOOT_SIZE = "256M"'
} > "${MACHINE_FOLDER}/${MACHINE_FAMILY}"
