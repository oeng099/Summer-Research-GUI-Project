#Working Log

#8 December 2021
Set up the EmotionGUI project with JavaFX.Created three scenes using SceneBuilder: HomeScreen, AnnotationScreen and VisualiseScreen. Research into how to implement a model that will be able to display its fractional values when the mouse is moved around on it will need to be done to be able to properly start to fill out the visualise and annotation scenes. 

#9 December 2021
Implemented functionality for buttons on HomeScreen scene and added in the componenets for the VisualiseScreen scene. AnnotationScreen will likely be blank for now ways to potentialluy implement its functionality are looked into first.

#10 December 2021
Initial functionality for select file button implemented. Main menu button functionality in VisualScreen scene implemented.

#13 December 2021
Added in a scatter plot to represent the valence-arosual model. Also added in functionality for the manual plot. Need to look into scatter plot further to stop it from changing scale.

#14 December 2021
Improved Scatter Chart valence-arousal model so that it now does not change its axes when a series is added. The duplicate series error regardining plotting multiple manual points was also resolved. Clear and save buttons were added to visualise screen with clear functionality implemented.

#15 December 2021
Changed the axes for the valence-arousal model to -1 to 1, following the meeting with my supervisor today. Also added in the outline of a circle on the model to put on landmark emotions in the future.

#16 December 2021
Added in error handling to display an error message when plotting manually on the visualise screen. This protects against trying to plot when no numbers have been entered, or if a number entered is outside of the acceptable range.

#17 December 2021
Downloaded in the Opencsv project to help write to and read csv files. This allowed for the implemenentation of the save method linked to the save button in the visualise screen.

#20 December 2021
Added in a separate save button to be able to save the model as a png. Refactored the implementation of the circle so that it shows up on the saved png.

#21 December 2021
Implemented plot file functionality - csv files are now able to be plotted on the model. Implemented functionality for showing the details of a point when hovering over it.

#04 January 2022
Added in the hover label function to points plotted from csv files. Landmark emotions were added in to the intialisation of the visualise screen. These emotions and coordinates were used from the paper 'What a neural net needs to know about emotion words' - acknowledgement of the paper needs to be added into the app later. Css files were created for the home and visualise screen to change the background and text to make them more visually appealing. 

#05 January 2022
Changed the font on all screens to make it more readable. Moved text on the left side of the visualise screen slightly to the right. 

#06 January 2022
Changed the background to white for greater readability. Added in an additional button to differentiate between plotting a CSV or WAV file. Implemented functionality to show the details of a landmark emotion point when hovered over.

#07 January 2022
Added in an information button for the required format of the CSV file and implemented its functionality.

#10 January 2022
Made minor changes to the visualise screen regarding the initial series that displays the 'Landmark Emotions'. Added to the README file for the project. Looked into the model that will be used to convert WAV files to CSV files - there is a slight issue in terms of testing the model out.

#11 January 2022
Added hover labels to nodes plotted either manually or through a CSV file. Css added to the help screen for CSV files. Fixed up minor errors when loading the home screen from the visualise screen. 

#12 January 2022
Enlarged the size of both the home screen and visualise screen to better accommodate potential media played on the annotation screen. Added the demo code for the model to a new ipynb file in colabs, access to files on a drive needed.

#13 January 2022
Working on getting the model to work. The demo model works on colabs after some modifications to the code however not offline yet. Looked into adding permanent labels to nodes in the visualise screen while still having them show specific coordinates when hovered over.

#14 Janury 2022
Continued to try to get the group108aRsLSTM.py script to run, however the script may be too memory-intensive. Currently unclear on how to transfer this previously written software into my project. The specific file that will actually convert a WAV file to CSV file using the model is also unclear as there isn't much direction from the README file they provided. 

#17 January 2022
Changed the axis of the to change the negative zero value. Modifided the demo code in the model project so that it now creates a CSV file with valence and arousal values. 

#18 January 2022
Got the demo code to now work with my GUI, thus the select and plot WAV file functionality is all completed. Added in the necessary files from the ESCE-Part-IV-700-108 folder to my project so the python script to convert WAV files to CSV files will run. Implemented changing models used, this was done through a new screen and controller, and rewrites the lines in the python script responsible for the model used. There is currently still an issue with the allFileCombineU.csv being too big to upload on Github, looking into possible solutions such as git lf.

#19 January 2022
Changed points in scatter chart from orange to transparent so all that is left is the label. Fixed issues with changing the model in the python script causing issues in converting the WAV to CSV file.

#20 January 2022
Changed scatter chart circle to fit properly. Changed initial point colour to transparent.

#23 January 2022
Added in mediaView for annotation screen so that media can be loaded into it. Select and play buttons implemented on annotation screen as well. Scatter chart of the model was also added in on a smaller scale on annotation screen.

#24 January 2022
Added in getting the mouse position when it is over the chart on visualise screen. Done for testing, will need to be changed back later.

#27 January 2022
Changed size for all screens. Added in getting coordianate when over the scatter chart using mouse position on the model in the annotation screen.

#28 January 2022
Implemented fuctionality for plotting points by clicking on the model in annoation screen. Added in media control buttons in annotation screen, still need to add their functionality.

#30 January 2022
Added hover labels to the points added onto the annotation screen by clicking. Added in functionality of auto clicking on the annotation screen.

#31 January 2022
Implemented the play/pause button onto the annotation screen. Added in a way for the annotation to end when the media is finished playing. Added in forward and backward functionality for those buttons on the annotation screen. The auto clicking needs to be further improved as there are issues when any media control button is pressed. 
