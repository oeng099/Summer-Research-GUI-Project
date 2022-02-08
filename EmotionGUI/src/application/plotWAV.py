#!/usr/bin/env python

import matplotlib.pyplot as plt
import numpy as np
import wave
import sys
import os

from matplotlib.pyplot import figure
figure(figsize=(10,8))

wavfile = sys.argv[1]
spf = wave.open(wavfile,"r")

signal = spf.readframes(-1)
signal = np.fromstring(signal, "Int16")
fs = spf.getframerate()

Time = np.linspace(0,len(signal)/fs,num = len(signal))

fileNameSplit = sys.argv[1].split("/")
highestIndexSplit = len(fileNameSplit) - 1
wavfilename = fileNameSplit[highestIndexSplit].replace('.wav','')
waveformTitle = wavfilename + " Audio Waveform"
waveformImageName = wavfilename + "_Audio_Waveform.png"

plt.figure(1)
plt.title(waveformTitle)
plt.xlabel("Time")
plt.ylabel("Amplitude")
plt.plot(Time,signal)

newPath = os.path.join(os.getcwd(),"images/audio_waveforms")
os.chdir(newPath)
plt.savefig(waveformImageName)
