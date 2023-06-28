import processing.core.PApplet;
import processing.core.PImage;

class Circuits extends PApplet{

    public static void main(String[] args) {
        runSketch(new String[] {
                "Create a Circuit Tree, Save to Image by Pressing 's'"
            },
            new Circuits());
    }

    PImage image; //<>//
    public void settings() {
        image = loadImage("./DoNotTouch/empty_tree.png");
        size(image.width, image.height);
    }

    public void setup() {
        background(255);
        image(image, 0, 0);
        println("Done");
    }

    boolean isInSkillPoint(Point p) {
        return p.x >= size && p.y >= size;
    }

    public void mousePressed() {
        MidPoint max = getBestWhiteCross(mouseX, mouseY);
        println("hor " + max.cross.x + " ver " + max.cross.y + " x " + max.coord.x + " y " + max.coord.y);
        loadPixels();
        if (isInSkillPoint(max.cross)) {
            for (int y = max.coord.y - size / 2 - 1; y < max.coord.y + size / 2; y++) {
                for (int x = max.coord.x - size / 2 - 1; x < max.coord.x + size / 2; x++) {
                    if (pixels[y * width + x] == color(255)) {
                        pixels[y * width + x] = color(255, 0, 255);
                    } else if (pixels[y * width + x] == color(255, 0, 255)) {
                        pixels[y * width + x] = color(255);
                    }
                }
            }
        }
        updatePixels();
    }

    int size = 21;

    class MidPoint {
        Point coord;
        Point cross;
        MidPoint(Point coord, Point cross) {
            this.coord = coord;
            this.cross = cross;
        }
    }

    MidPoint getBestWhiteCross(int startx, int starty) {
        image.loadPixels();
        if (alpha(image.pixels[starty * width + startx]) != 0) return new MidPoint(new Point(0, 0), new Point(0, 0));
        MidPoint max = new MidPoint(new Point(0, 0), new Point(0, 0));
        int count = 0;
        for (int y = starty - size / 2; y < starty + size / 2; y++) {
            for (int x = startx - size / 2; x < startx + size / 2; x++) {
                Point current = getCurrentWhiteCross(x, y);
                if (current.x == size * 2 && current.y == size * 2) count++;
                if (isInSkillPoint(current) && current.x >= max.cross.x && current.y >= max.cross.y) {
                    max.cross = current;
                    max.coord.x = x;
                    max.coord.y = y;
                }
            }
        }
        return (count >= size / 2) ? new MidPoint(new Point(0, 0), new Point(0, 0)) : max;
    }

    Point getCurrentWhiteCross(int x, int y) {
        image.loadPixels();
        if (y < 0 || y >= height || x < 0 || x >= width) {
            return new Point(0, 0);
        } else {
            int alpha = (int) alpha(image.pixels[y * width + x]);
            if (alpha != 0)
                return new Point(0, 0);
        }
        int hor = 0;
        int ver = 0;
        boolean horP = true;
        boolean horM = true;
        boolean verP = true;
        boolean verM = true;
        for (int i = 0; i < size; i++) {
            if (horP) {
                int index = y * width + x + i;
                if (index >= 0 && index < image.pixels.length && alpha(image.pixels[index]) == 0) hor++;
                else horP = false;
            }
            if (horM) {
                int index = y * width + x - i;
                if (index >= 0 && index < image.pixels.length && alpha(image.pixels[index]) == 0) hor++;
                else horM = false;
            }
            if (verP) {
                int index = (y + i) * width + x;
                if (index >= 0 && index < image.pixels.length && alpha(image.pixels[index]) == 0) ver++;
                else verP = false;
            }
            if (verM) {
                int index = (y - i) * width + x;
                if (index >= 0 && index < image.pixels.length && alpha(image.pixels[index]) == 0) ver++;
                else verM = false;
            }
        }
        return new Point(hor, ver);
    }

    class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void keyReleased() {
        if (key == 's') saveFrame("circuitTree.png");
    }

    public void draw() {}
}