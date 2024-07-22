import java.util.Random;

public class Board {
    private final int width, height, numRed, numBlue;
    private Square[][] board;
    public Board(int width, int height, int numRed, int numBlue){
        this.width = width;
        this.height = height;
        this.numRed = numRed;
        this.numBlue = numBlue;
        board = new Square[width][height];
        int redCount = numRed, blueCount = numBlue;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(redCount > 0){
                    board[i][j] = new Square(Square.Color.Red);
                    redCount--;
                } else if (blueCount > 0) {
                    board[i][j] = new Square(Square.Color.Blue);
                    blueCount--;
                }
                else{
                    board[i][j] = new Square(Square.Color.White);
                }
            }
        }
        Random random = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                Square temp = board[i][j];
                board[i][j] = board[x][y];
                board[x][y] = temp;
            }
        }
    }

    // tolearancePercent = % of the same color
    public void leaveOrStay(int x, int y, double tolerancePercent){
        int tolerance = (int) (((double) countAdjacent(x, y)) * tolerancePercent);
        if (countSame(x, y) < tolerance){
            boolean success = false;
            int counter = 0;
            while(!success){
                Random random = new Random();
                int i = random.nextInt(0, board.length);
                int j = random.nextInt(0, board[0].length);
                if (board[i][j].color == Square.Color.White && countSame(i, j, board[x][y].color) >= tolerance) {
                    board[i][j] = board[x][y];
                    board[x][y] = new Square(Square.Color.White);
                    success = true;
                    if(counter == 1000){
                        return;
                    }
                } else {
                    counter++;
                }
            }
        }
    }

    private int countSame(int x, int y){
        int countSame = 0;
        if(x - 1 >= 0){
            if(y - 1 >= 0){
                if(board[x][y].color == board[x-1][y-1].color){
                    countSame++;
                }
            }
            if(board[x][y].color == board[x-1][y].color){
                countSame++;
            }
            if(y + 1 < height){
                if(board[x][y].color == board[x-1][y+1].color){
                    countSame++;
                }
            }
        }
        if(x + 1 < width){
            if(y + 1 < height){
                if(board[x][y].color == board[x+1][y+1].color){
                    countSame++;
                }
            }
            if(board[x][y].color == board[x+1][y].color){
                countSame++;
            }
            if(y - 1 >= 0){
                if(board[x][y].color == board[x+1][y-1].color){
                    countSame++;
                }
            }
        }
        if(y + 1 < height){
            if(board[x][y].color == board[x][y+1].color){
                countSame++;
            }
        }
        if(y - 1 >= 0){
            if(board[x][y].color == board[x][y-1].color){
                countSame++;
            }
        }
        return countSame;
    }

    private int countSame(int x, int y, Square.Color color){
        int countSame = 0;
        if(x - 1 >= 0){
            if(y - 1 >= 0){
                if(color == board[x-1][y-1].color){
                    countSame++;
                }
            }
            if(color == board[x-1][y].color){
                countSame++;
            }
            if(y + 1 < height){
                if(color == board[x-1][y+1].color){
                    countSame++;
                }
            }
        }
        if(x + 1 < width){
            if(y + 1 < height){
                if(color == board[x+1][y+1].color){
                    countSame++;
                }
            }
            if(color == board[x+1][y].color){
                countSame++;
            }
            if(y - 1 >= 0){
                if(color == board[x+1][y-1].color){
                    countSame++;
                }
            }
        }
        if(y + 1 < height){
            if(color != board[x][y+1].color){
                countSame++;
            }
        }
        if(y - 1 >= 0){
            if(color != board[x][y-1].color){
                countSame++;
            }
        }
        return countSame;
    }

    private int countAdjacent(int x, int y){
        if(x == 0 && y == 0 || x == board.length && y == 0 || x == 0 && y == board[0].length || x == board.length && y == board[0].length){
            return 3;
        } else if(x == 0 || y == 0 || x == board.length || y == board[0].length){
            return 5;
        } else {
            return 8;
        }
    }

    public Square[][] getBoard(){
        return board;
    }
}
