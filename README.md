# SimpleBeepsMaker
Simple project for generating beeping WAV files 

To generate WAV file with several beeps - each one with different frequency, it's needed to define them in config.properties file.
This example of config.properties
```
no_of_intervals=3
sample_rate=44100
n0_duration=100
n0_freq=500
n1_duration=200
n1_freq=0
n2_duration=300
n2_freq=750
```
is describing sound that beeps with frequency of 500 Hz for 100 ms then is silent for 200 ms and finally beeps with frequency of 750 Hz for 300 ms.
