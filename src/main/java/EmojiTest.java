import javax.swing.*;

//Image emoji constructor
//Author: Arukhan
class ImageViewerFrame extends JFrame{
    JLabel label;
    //Constructor
    public ImageViewerFrame() {
        setSize(400,400);
        //Use a label to display the image
        label = new JLabel();
        add(label);
        label.setIcon(new ImageIcon("C:\\Users\\Арухан\\IdeaProjects\\TestApp\\HackathonProject2022\\src\\main\\resources\\EmojiImages\\Angry.png"));
    }

    public void changeNameXO(String name){
        label.setIcon(new ImageIcon("C:\\Users\\Арухан\\IdeaProjects\\TestApp\\HackathonProject2022\\src\\main\\resources\\EmojiImages\\"+ name +".png"));
    }
}





