# Copyright 2021-2026 CompuLab
# Make GoPoint demos available on the CompuLab i.MX8MP family.

# demos.json contains ${MACHINE}, so the package cannot be shared between
# machines even when they use the same CPU architecture.
PACKAGE_ARCH:compulab-mx8mp = "${MACHINE_ARCH}"

do_install:prepend:compulab-mx8mp() {
	sed -i "s/\(\"compatible.*imx8mp.*\)\",/\1, ${MACHINE}\", /g" \
		${S}/demos.json
}
