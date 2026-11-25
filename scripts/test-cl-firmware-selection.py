#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Check bootloader selection without accessing EEPROM, eMMC or reboot paths."""

import os
from pathlib import Path
import subprocess
import tempfile


ROOT = Path(__file__).resolve().parents[1]
SCRIPT = ROOT / 'recipes-desktop/cl-firmware/files/cl-firmware.sh'
TARGETS = (
    'iot-gate-imx8plus', 'iot-gate-imx8pv2', 'iotdin-imx8p',
    'mcm-imx8m-plus', 'mcm-imx8pv2', 'som-imx8m-plus',
    'ucm-imx8m-plus', 'ucm-imx8m-plus-sbev',
)
ALIASES = {
    'iotdin-imx8p': 'iotdin-imx8p',
    'iotg-imx8plus': 'iot-gate-imx8plus',
    'mcm-imx8plus': 'mcm-imx8m-plus',
    'ucm-imx8plus': 'ucm-imx8m-plus',
    'som-imx8plus': 'som-imx8m-plus',
}


def main():
    subprocess.run(['bash', '-n', str(SCRIPT)], check=True)
    # Keep only the naming helper and maps; exclude every update/reboot function.
    prefix = SCRIPT.read_text().split('nothing_to_do()', 1)[0]
    prefix = prefix.replace('/dev/kmsg', '"${TEST_LOG}"')
    mock = r'''
eeprom-util() {
    echo read >> "${TEST_COUNTER}"
    printf 'Product Name: %s\nProduct Options: %s\n' "$TEST_PRODUCT" "$TEST_DRAM"
}
firmware_name
'''
    cases = {target: target for target in TARGETS}
    cases.update(ALIASES)
    cases['MCM-IMX8PV2'] = 'mcm-imx8pv2'
    cases['unknown-device'] = 'empty_eeprom'
    cases[''] = 'empty_eeprom'
    with tempfile.TemporaryDirectory(prefix='cl-firmware-selection-') as directory:
        work = Path(directory)
        for product, target in cases.items():
            for dram in ('D1', 'D2', 'D4', 'D8', ''):
                counter = work / 'reads'
                counter.unlink(missing_ok=True)
                env = dict(os.environ, TEST_PRODUCT=product, TEST_DRAM=dram,
                           TEST_LOG=str(work / 'log'), TEST_COUNTER=str(counter))
                result = subprocess.run(['bash', '-s'], input=prefix + mock,
                                        text=True, capture_output=True, env=env,
                                        check=True, timeout=10)
                assert result.stdout.strip() == 'imx-boot_' + target, (
                    product, dram, result.stdout)
                assert counter.read_text().splitlines() == ['read']
    print('PASS: all-capacity firmware names, legacy aliases, unknown products '
          'and DRAM-independent selection')


if __name__ == '__main__':
    main()
