#!/bin/bash -x

MACHINE_FOLDER=${BUILDDIR}/../sources/meta-bsp-imx8mp/conf/machine
MACHINE_PATTERN="UBOOT_CONFIG"
MACHINE_FAMILY="compulab-imx8mp.conf"
MACHINE_MAIN="ucm-imx8m-plus"

: >  ${MACHINE_FOLDER}/${MACHINE_FAMILY}

for M in $(grep -r ${MACHINE_PATTERN}  ${MACHINE_FOLDER}/ | awk -F":" '($0=$1) { n=split($0,a,"/"); gsub(/.conf/,"",a[n]); print a[n]}' | sort -u); do
	mkdir -p ${MACHINE_FOLDER}/include/device-tree;
	MACHINE=${M} bitbake-getvar -r linux-compulab KERNEL_DEVICETREE | awk -F"=" '(/^KERNEL_DEVICETREE/)&&($0="KERNEL_DEVICETREE += "$2)' > ${MACHINE_FOLDER}/include/device-tree/${M}.inc;
	echo "require include/device-tree/${M}.inc" >> ${MACHINE_FOLDER}/${MACHINE_FAMILY}
done

sed -i "1i require ${MACHINE_MAIN}.conf" ${MACHINE_FOLDER}/${MACHINE_FAMILY}
sed -i '$ a\\nWKS_BOOT_SIZE = "256M"' ${MACHINE_FOLDER}/${MACHINE_FAMILY}
