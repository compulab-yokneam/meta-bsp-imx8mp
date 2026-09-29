# Fetch CANopenNode as part of do_fetch. Network access is intentionally not
# available to do_compile, so the recipe must not run git submodule update
# while compiling.
SRC_URI:compulab-mx8mp = "gitsm://github.com/CANopenNode/CANopenLinux.git;branch=master;protocol=https"

do_compile:compulab-mx8mp() {
	oe_runmake -C ${S}
	oe_runmake -C ${S}/cocomm
}
