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
