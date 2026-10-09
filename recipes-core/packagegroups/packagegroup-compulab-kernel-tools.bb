# SPDX-License-Identifier: MIT

SUMMARY = "Linux kernel source-tree tools for CompuLab images"
DESCRIPTION = "Installs the target-side Linux utilities built from the active \
kernel's tools directory by the recipes available in this BSP."
LICENSE = "MIT"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Keep this list at recipe/package granularity.  The individual recipes carry
# the cross-compilation flags, dependencies, kernel-version coupling, and
# install fixes required by their respective tools subdirectories.
RDEPENDS:${PN} = " \
    bpftool \
    cpupower \
    kernel-selftest \
    kernel-tools-iio \
    kernel-tools-pci \
    kernel-tools-virtio \
    kernel-tools-vsock \
    perf \
    perf-archive \
    perf-python \
    perf-tests \
    rtla \
    spidev-test \
    tmon \
    usbip-tools \
"
