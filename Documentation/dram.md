# Get the product DRAM configuration

The device DRAM configuration is at the device label and is also stored in the device EEPROM.

* Getting memory configuration from the label:
Use the 'D' option code from the device label.

* Getting memory configuration from the EEPROM:

| Environment | Command | Output | Description |
|---|---|---|---|
| U-Boot| i2c dev 1; i2c md 0x50 0x90 0x10|0090: 43 31 38 30 30 51 4d 2d 44 34 2d 4e 33 32 2d 00    C1800QM-D4-N32-.| D4 = 4G
| Kernel| i2cdump -f -y 1 0x50 2>/dev/null \| awk '/^90:/' |0: 43 31 38 30 30 51 4d 2d 44 34 2d 4e 33 32 2d 00    C1800QM-D4-N32-.| |

* Mapping of product DRAM configuration to DRAM_CONF:

|Product DRAM Option|DRAM_CONF|
|---|---|
|D1,D8|d1d8|
|D2,D4|d2d4|
