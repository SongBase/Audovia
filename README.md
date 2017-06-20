	# Audovia
Database application for making music using JFugue MusicStrings

**_[Website](http://audovia.com/)_**

Make music on your Linux Ubuntu laptop or PC with **Audovia**.  Songs can have up to 15 instrumental voices and a percussion track with instruments chosen from a soundbank of 128 instruments.  Either the default soundbank or one of your own can be used.  Songs can be developed, tested and edited very quickly and easily by virtue of the database structure and the JFugue MusicString notation.

Notes are specified by their name and octave or by their MIDI value and their durations are specified either by character code, or numerically.

You can use notes from C0 to G10, corresponding to MIDI values 0 to 127.  Middle C is C5.

Notes can be entered manually or by picking from graphic Treble, Alto, Tenor and Bass staves within the MusicString editor.

Within the application, use *File/Template* to create a song, then *File/Tree View* and expand the nodes.  Select each bar in turn and enter the notes.  Select "Song" and press Play to play back your music.  From the *File* menu, you can export to MIDI and MusicXML for music processing and music publishing.  The MIDI files can be opened in LMMS and exported to WAV.  WAV files can be opened in Audacity, then exported to MP3.  The MusicXML files can be opened in MuseScore for music publishing.

You can collaborate with your colleagues on a song by using a MySQL shared database.  You can also export and import songs in XML format.  Use *File/XML Import* to import the demonstration songs.

If you are setting up a shared database, use the *Database/Database Connections* form to enter the connection parameters obtained from your Database as a Service provider.  Then use *File/Create Tables* to create the database tables on the remote host.

We recommend that you create a user in your MySQL database called 'guest' with *EXECUTE* as the only privilege.  Connecting as 'guest' enables read access to all songs and also the ability to create and log in to individual user accounts for the creation of songs.  Full access to these songs can optionally be shared with selected other user accounts.
