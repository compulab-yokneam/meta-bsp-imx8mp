# meta-compulab-bsp replaces the upstream gitsm fetch with two independent
# repositories. Restore gitsm so BitBake checks out the submodule revision
# recorded by the nnshark source tree directly into ${S}/common.
NNSHARK_SRC:compulab-mx8mp = "gitsm://github.com/nxp-imx/nnshark.git;protocol=https;name=nnshark"

SRC_URI:remove:compulab-mx8mp = "git://gitlab.freedesktop.org/gstreamer/common.git;protocol=https;branch=master;name=common;destsuffix=git/common"
SRCREV_FORMAT:compulab-mx8mp = "nnshark"
