# EmotionGUI

A GUI to visualise and annotate emotion coordiantes on a two-dimensional model.

### Contributors:

- Owen Eng (https://github.com/oeng099)

### Project Description:

This project has two objects: plot emotional coordinates that are entered into the GUI onto a two-dimensional model and allow the user to annotate their emotions on a two-dimensional model while viewing a piece of media. 


### Requirements:

EmotionGUI requires the following to run:

- Javafx SDK (11.02 or later)
- Python3

### How to run:

1. From the Summer-Research-GUI-Project folder, switch into EmotionGUI/src/application/WAV_To_CSV/models and extract the allFileCombineU.zip file into the same folder.

2. Open terminal and change into the EmotionGUI folder.

3. In terminal run "java -Djdk.gtk.version=2 --module-path copy/path/to/javafx/sdk/lib/folder --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml -jar EmotionGUI.jar"

An example of the above java -Djdk.gtk.version=2 --module-path Downloads\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.media,javafx.base,javafx.fxml  -jar EmotionGUI.jar


# How to use:

When the software loads it will start with the home screen with visualise and annotation buttons. Clicking either button will lead to their respective screens.


### Visualisation

On the visualisation screen there are three ways to enter coordiantes to be plotted.

For 'Select CSV File', pressing the button will open up a window for the user to select a CSV file to plot. Next to the 'Select CSV File' button is an information button which outines the format the CSV file is required to have for the GUI to be able to plot the points. Once a CSV file is selected, press the 'Plot' button to the right of the information button to plot the coordiantes on the model.

Plotting a WAV File is similar to plotting a CSV file. Press 'Select WAV File' to choose the file to plot and then press the plot button next to it. 

To plot emotions manually, the user can input values within the allowed range in the textfields next to the 'Valence:' and 'Arousal:' text and then press the plot button next to it. 

There is also the option to change the model that the machine-learning program uses to predict the emotional coordinates of WAV files. This is done through the 'Change model' button and changes the tool to a new screen that allows for selection of hdf5 files.

### Annotation

Annotation currently supports WAV and mp4 files. To annotate, click select file and choose the media file to annotate. Once loaded press 'play' besides the select file button and the media and annotation will begin in five seconds.

For annotation, move the mouse to the position on the valence-arousal model that represents your preceived emotion at a given point. 

To pause and play, press the 'p' key. All other forms of media control are below the media. Be noted that backwards and forwards will move the media by five seconds in their respective directions.