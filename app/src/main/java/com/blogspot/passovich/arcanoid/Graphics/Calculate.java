package com.blogspot.passovich.arcanoid.Graphics;

import android.content.Context;

import com.blogspot.passovich.arcanoid.Data.Properties;
import com.blogspot.passovich.arcanoid.Elements.Ball;
import com.blogspot.passovich.arcanoid.Elements.Balls;
import com.blogspot.passovich.arcanoid.Elements.Bonus;
import com.blogspot.passovich.arcanoid.Elements.Bonuses;
import com.blogspot.passovich.arcanoid.Elements.Brick;
import com.blogspot.passovich.arcanoid.Elements.Bricks;
import com.blogspot.passovich.arcanoid.Elements.Bullet;
import com.blogspot.passovich.arcanoid.Elements.Bullets;
import com.blogspot.passovich.arcanoid.Elements.Bumper;
import com.blogspot.passovich.arcanoid.Elements.Gun;
import com.blogspot.passovich.arcanoid.Elements.Rocket;

import java.util.Random;

public class Calculate {
    private static final String TAG = "myLogs";
    private static double lX, lY, rX, rY, uX, uY, dX, dY;       //координаты крайних точек шарика

    public static int bulletMove (Bullets bullets, Bricks bricks, Bonuses bonuses){
        if (!bullets.bulletList.isEmpty()) {
            for (Bullet b : bullets.bulletList) {
                if (b.y<=0) {
                    bullets.bulletList.remove(b);
                    return 3;
                }
                if (bulletHitBrickCheck(b,bricks.bricksMatrix,bonuses)){
                    bullets.bulletList.remove(b);
                    return 3;
                }
            }
        }
        bullets.step();
        return 0;
    }
    public static int rocketMove (Rocket rocket, Bricks bricks, Bonuses bonuses){
        if(rocketHitBrickCheck(rocket, bricks.bricksMatrix, bonuses)){
            return 3;
        }
        if(rocket.isFireAccepted()){rocket.moveOneStep();}
            return 0;
    }
    public static int bonusMove(
            Bonuses bonuses,
            Bumper bumper,
            Properties p,
            Balls balls,
            Bullets bullets,
            Gun gun,
            Rocket rocket
    ){
       if (!bonuses.bonusList.isEmpty())
            for(Bonus b:bonuses.bonusList) {
                b.moveOneStep();
                if (bumperHitBonusCheck(bumper, b)){
                    applyBonus(b.getType(), p, balls, bumper, bullets, gun, rocket);//применяем бонус
                    bonuses.bonusList.remove(b);return 6;} //удаляем пойманный бонус из коллекции
                if (b.y>p.SCREEN_HEIGHT){bonuses.bonusList.remove(b);return 0;}
        }
        return 0;
    }
    public static int ballsMove(
            Context context,
            Bumper bumper,
            Balls balls,
            Properties p,
            Bricks bricks,
            Bonuses bonuses,
            Gun gun,
            Rocket rocket
    ){
        int outParameter=0;
        for(Ball ball:balls.ballList){
            //расчитываем крайние точки шарика
            lX = ball.x - ball.r;    lY = ball.y;
            rX = ball.x + ball.r;    lY = ball.y;
            uX = ball.x;             uY = ball.y-ball.r;
            dX = ball.x;             dY = ball.y+ball.r;

            //проверяем на касание кирпича
            if (ballHitBrickCheck(bricks.bricksMatrix, ball, bonuses)){
                p.scoresAdd(1);
                outParameter = 2;
                ball.lastElementTouch = ball.NOT_BUMPER;
            }
            //проверяем на касание ракетки
            if (ballHitBumperCheck(bumper, ball)){
                if (bumper.magnetActivated() && !bumper.ballCatched()){
                    ball.setXBumperTouch(ball.x - bumper.x); //смещение на ракетке
                    ball.moveAccept = false;
                    bumper.ballCatch();
                }
                outParameter = 3;
                ball.lastElementTouch = ball.BUMPER;
            }
            //Проверяем на касание левого, правого и верхнего краёв экрана
            if (ballHitScreenBorderCheck(p, ball)){
                outParameter = 3;
                ball.lastElementTouch = ball.NOT_BUMPER;
            }
            //Смещаем шарик(если разрешено движение шарика), или прилепляем его к ракетке
            if (ball.moveAccept){
                ball.moveOneStep();
            }else {ball.x = bumper.x+ball.getXBumperTouch(); ball.y = bumper.y - ball.width / 2 - 1;}
        }
        //проверяем на уход шариков шариков за нижний край экрана
        if (balls.ballList.size() > 1){
            for(Ball ball:balls.ballList){
                if (ball.y >= p.SCREEN_HEIGHT){balls.ballList.remove(ball); break;}
            }
        }else //проверка на потерю последнего шарика
            if (balls.ballList.get(0).y >= p.SCREEN_HEIGHT){
                balls.ballList.get(0).moveAccept = false;
                balls.ballList.get(0).moveReset();
                p.lives--;
                bonusesDeactivate(bumper);
                rocket.setFireAcceptFalse();
                gun.setDrawable(false);
                if(looseCheck(p)) {return -1;}
            }
        //возвращаем звуковой эффект
        return outParameter;
    }
    public static boolean winnCheck(Bricks bricks){
        //проверка все ли кирпичи выбиты
        for (int i = 0; i < bricks.bricksMatrix.length; i++){
            for (int j = 0; j < bricks.bricksMatrix[i].length; j++){
                if (bricks.bricksMatrix[i][j].lives > 0) return false;
            }
        }
        return true;
    }
    public static boolean looseCheck(Properties p){
        if (p.lives <= 0) {return true;}
        return false;
    }

    private static void bonusesDeactivate(Bumper bumper){
        bumper.magnetDeActivate();
    }
    private static void applyBonus(
            int bonusType,
            Properties p,
            Balls balls,
            Bumper bumper,
            Bullets bullets,
            Gun gun,
            Rocket rocket
    ){
        switch (bonusType){
            case 0: p.scoresAdd(10); break;
            case 1: p.scoresAdd(100); break;
            case 2: p.lives++; break;
            case 3: balls.getThreeBalls(); break;
            case 4: balls.getEightBalls(); break;
            case 5: bumper.setBigBumper(); break;
            case 6: bumper.setSmallBumper(); break;
            case 7: bumper.setNormalBumper(); break;
            case 8: bumper.magnetActivate(); break;
            case 9: bullets.setFireAcceptTrue(); gun.setDrawable(true); break;
            case 10:rocket.setFireAcceptTrue(); break;
            case 11:p.MULTIPLIER_BALL_SPEED *= 1.2; break;
            case 12:p.MULTIPLIER_BALL_SPEED *= 0.8; break;
            case 13:
                int bT = new Random().nextInt(10);
                applyBonus(bT, p, balls, bumper, bullets, gun, rocket);
                break;
        }
    }

    private static Boolean ballHitBrickDownSideCheck(Brick bricks[][], int i, int j, double x){
        if ((uY < (bricks[i][j].y + bricks[i][j].height)) && uY > bricks[i][j].y){
            if(x > bricks[i][j].x && (x < (bricks[i][j].x + bricks[i][j].width))){
                return true;
            }
        }
        return false;
    }
    private static Boolean ballHitBrickUpSideCheck(Brick bricks[][], int i, int j, double x){
        if ((dY > bricks[i][j].y) && (dY<(bricks[i][j].y + bricks[i][j].height))){
            if(x>bricks[i][j].x && (x < (bricks[i][j].x + bricks[i][j].width))){
                return true;
            }
        }
        return false;
    }
    private static Boolean ballHitBrickLeftSideCheck(Brick bricks[][], int i, int j, double y){
        if ((rX > bricks[i][j].x) && (rX < (bricks[i][j].x + bricks[i][j].width))) {
            if (y > bricks[i][j].y && (y < (bricks[i][j].y + bricks[i][j].height))) {
                return true;
            }
        }
        return false;
    }
    private static Boolean ballHitBrickRightSideCheck(Brick bricks[][], int i, int j, double y){
        if ((lX < (bricks[i][j].x + bricks[i][j].width)) && lX > bricks[i][j].x){
            if(y > bricks[i][j].y && (y < (bricks[i][j].height + bricks[i][j].y))){
                return true;
            }
        }
        return false;
    }
    private static boolean ballHitBrickCheck(Brick bricks[][], Ball ball, Bonuses bonuses){
        //проверяем на касание кирпича
        for (int i = 0; i < bricks.length; i++){
            for (int j = 0;j < bricks[i].length; j++){
                if (bricks[i][j].lives > 0){
                    //Касание угла
                    if(ballHitBrickPointsCheck(bricks, i, j, ball)){
                        bricks[i][j].lives--;
                        if (bricks[i][j].lives == 0&&bricks[i][j].isBonus())
                            bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                        return true;
                    }
                    //касание грани
                    if (ballHitBrickDownSideCheck(bricks, i, j, ball.x)){
                        ball.yVectorInvert();
                        bricks[i][j].lives--;
                        if (bricks[i][j].lives == 0 && bricks[i][j].isBonus())
                            bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                        return true;
                    }
                    //Проверка на касание верхнего края кирпича
                    if (ballHitBrickUpSideCheck(bricks, i, j, ball.x)){
                        ball.yVectorInvert();
                        bricks[i][j].lives--;
                        if (bricks[i][j].lives == 0 && bricks[i][j].isBonus())
                            bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                        return true;
                    }
                    //Проверка на касание левого края кирпича
                    if (ballHitBrickLeftSideCheck(bricks, i, j, ball.y)){
                        ball.xVectorInvert();
                        bricks[i][j].lives--;
                        if (bricks[i][j].lives == 0 && bricks[i][j].isBonus())
                            bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                        return true;
                    }
                    //Проверка на касание правого края кирпича
                    if (ballHitBrickRightSideCheck(bricks, i, j, ball.y)){
                        ball.xVectorInvert();
                        bricks[i][j].lives--;
                        if (bricks[i][j].lives == 0 && bricks[i][j].isBonus())
                            bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private static boolean ballHitBrickPointsCheck(Brick bricks[][],int i,int j,Ball ball){
        //проверка на касание углов кирпича
        float luX, luY, ruX, ruY, ldX, ldY, rdX, rdY;//координаты углов кирпича
        luX=bricks[i][j].x;                        luY=bricks[i][j].y;
        ruX=bricks[i][j].width + bricks[i][j].x;   ruY=bricks[i][j].y;
        ldX=bricks[i][j].x;                        ldY=bricks[i][j].height + bricks[i][j].y;
        rdX=bricks[i][j].width + bricks[i][j].x;   rdY=bricks[i][j].height + bricks[i][j].y;

        if((Math.abs(luX - ball.x) < ball.r) && (Math.abs(luY - ball.y) < ball.r)){
            if (ball.getVectorX() == 1 && ball.getVectorY() == -1){ball.setVectorX(-1);}
            else
                if (ball.getVectorX() == -1 && ball.getVectorY() == 1){ball.setVectorY(-1);}
                else{ball.yVectorInvert(); ball.xVectorInvert();}
            return true;
        }
        if((Math.abs(ruX - ball.x) <= ball.r) && (Math.abs(ruY - ball.y) <= ball.r)){
            if (ball.getVectorX() == -1 && ball.getVectorY() == -1){ball.setVectorX(1);}
            else
                if (ball.getVectorX() == 1 && ball.getVectorY() == 1){ball.setVectorY(-1);}
                else{ball.yVectorInvert(); ball.xVectorInvert();}
            return true;
        }
        if((Math.abs(ldX - ball.x) <= ball.r) && (Math.abs(ldY - ball.y) <= ball.r)){
            if (ball.getVectorX() == 1 && ball.getVectorY() == 1){ball.setVectorX(-1);}
            else
                if (ball.getVectorX() == -1 && ball.getVectorY() == -1){ball.setVectorY(1);}
                else{ball.yVectorInvert(); ball.xVectorInvert();}
            return true;
        }
        if((Math.abs(rdX - ball.x) <= ball.r) && (Math.abs(rdY - ball.y) <= ball.r)){
            if (ball.getVectorX() == -1 && ball.getVectorY() == -1){ball.setVectorX(1);}
            else
                if (ball.getVectorX() == 1 && ball.getVectorY() == -1){ball.setVectorY(1);}
                else{ball.yVectorInvert(); ball.xVectorInvert();}
            return true;
        }
        return  false;
    }

    private static boolean ballHitBumperCheckUpSide(Bumper bumper,Ball ball){
        //проверяем на касание шариком верхней части ракетки
        if ((((ball.y + ball.r) >= bumper.y) && ((ball.y + ball.r) <= bumper.y + 20))
                && ((ball.x > (bumper.x - bumper.width / 2)) && (ball.x < (bumper.x + bumper.width / 2)))){
            return true;
        }
        return false;
    }
    private static boolean ballHitBumperCheckLeftSide(Bumper bumper, Ball ball){
        //проверяем на касание шариком левой части ракетки
        if ((rX > (bumper.x - bumper.width / 2)) && rX < bumper.x){
            if(ball.y > bumper.y && ball.y < bumper.y + bumper.height){
                return true;
            }
        }
        return false;
    }
    private static boolean ballHitBumperCheckRightSide(Bumper bumper, Ball ball){
        //проверяем на касание шариком правой части ракетки
        if ((lX < (bumper.x + bumper.width / 2)) && lX > bumper.x){
            if(ball.y > bumper.y && ball.y < bumper.y + bumper.height){
                return true;
            }
        }
        return false;
    }
    private static boolean ballHitBumperCheck(Bumper bumper, Ball ball){
        if (ballHitBumperCheckUpSide(bumper, ball) && ball.lastElementTouch != ball.BUMPER){
            ballMoveAngleCalculate(bumper, ball); return true;
        }
        if (ballHitBumperCheckLeftSide(bumper, ball) && ball.lastElementTouch != ball.BUMPER){
            ball.setVectorX(-1); return true;
        }
        if (ballHitBumperCheckRightSide(bumper, ball) && ball.lastElementTouch != ball.BUMPER){
            ball.setVectorX(-1); return true;
        }
        return false;
    }

    private static boolean ballHitScreenBorderCheck (Properties p, Ball ball){
        if(ball.moveAccept) {
            //проверяем на касание левого и правого краёв экрана
            if (lX <= 0 || rX >= p.SCREEN_WIDTH) {
                ball.xVectorInvert();
                return true;
            }
            //Проверяем на касание верхнего края экрана
            if (ball.y - ball.r <= 0 + p.STATUSBAR_HEIGHT) {
                ball.yVectorInvert();
                return true;
            }
        }
        return false;
    }
    private static boolean bumperHitBonusCheck(Bumper bumper, Bonus bonus){
        //проверяем на касание бонусом верхней части ракетки
        if ((((bonus.y + bonus.height) >= bumper.y) && ((bonus.y + bonus.height) <= bumper.y + bonus.height + (bumper.height)))
                && (((bonus.x + bonus.width) > (bumper.x - bumper.width / 2)) && (bonus.x < (bumper.x + bumper.width / 2)))){
            return true;
        }
        return false;
    }
    private static boolean bulletHitBrickCheck(Bullet bullet, Brick[][] bricks, Bonuses bonuses){
        for (int i = 0; i < bricks.length; i++){
            for (int j=0; j < bricks[i].length; j++){
                if (bricks[i][j].lives > 0) {
                    if ((bullet.y < (bricks[i][j].y + bricks[i][j].height)) && bullet.y > bricks[i][j].y) {
                        if (bullet.x > bricks[i][j].x && (bullet.x < (bricks[i][j].x + bricks[i][j].width))) {
                            bricks[i][j].lives--;
                            if (bricks[i][j].lives == 0 && bricks[i][j].isBonus())
                                bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean rocketHitBrickCheck(Rocket rocket,Brick[][] bricks,Bonuses bonuses){
        for (int i = 0; i < bricks.length; i++){
            for (int j = 0; j < bricks[i].length; j++){
                if (bricks[i][j].lives > 0) {
                    if ((rocket.y < (bricks[i][j].y + bricks[i][j].height)) && rocket.y > bricks[i][j].y) {
                        if (rocket.x > bricks[i][j].x && (rocket.x < (bricks[i][j].x + bricks[i][j].width))) {
                            bricks[i][j].lives--;
                            if (bricks[i][j].lives == 0 && bricks[i][j].isBonus())
                                bonuses.add(bricks[i][j].x, bricks[i][j].y, getBonusType());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static void ballMoveAngleCalculate(Bumper bumper, Ball ball){
        // Изменение угла движения шарика относительно оси Х
        if (ball.x > bumper.x){
            ball.angle = 90.f - ((ball.x - bumper.x) / (bumper.width / 140.0f));
            ball.setVectorX(1);
            ball.setVectorY(-1);
        }else{
            ball.angle = 90.f - ((bumper.x - ball.x) / (bumper.width / 140.0f));
            ball.setVectorX(-1);
            ball.setVectorY(-1);
        }
    }
    private static int getBonusType(){
        Random r = new Random();
        int probability = r.nextInt(181);
        if (probability>= 00 && probability<20 )return 0;   //10 очков
        if (probability>= 20 && probability<40 )return 1;   //100 очков
        if (probability>= 40 && probability<45 )return 2;   //жизнь
        if (probability>= 45 && probability<55 )return 3;   //3 шарика
        if (probability>= 55 && probability<60 )return 4;   //8 шариков
        if (probability>= 60 && probability<70 )return 5;   //Большая ракетка
        if (probability>= 70 && probability<80 )return 6;   //Маленькая ракетка
        if (probability>= 80 && probability<90 )return 7;   //Нормальная ракетка
        if (probability>= 90 && probability<100)return 8;   //магнит(прилипалка)
        if (probability>=100 && probability<107)return 9;   //Пулемёт
        if (probability>=107 && probability<112)return 10;  //Ракетница
        if (probability>=112 && probability<135)return 11;  //Увеличение скорости
        if (probability>=132 && probability<170)return 12;  //Уменьшение скорости
        if (probability>=170 && probability<180)return 13;  //Случайный приз
        return 13;
    }
}
