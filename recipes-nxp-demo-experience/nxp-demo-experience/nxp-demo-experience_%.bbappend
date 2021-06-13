# Copyright 2021 CompuLab
# Make it work with the ucm-imx8m-plus boards
do_install_prepend() {
	sed -i "s/imx8mpevk/${MACHINE}/" ${WORKDIR}/demos/demos.json
}
