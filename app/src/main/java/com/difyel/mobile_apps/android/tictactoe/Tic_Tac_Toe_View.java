package com .difyel .mobile_apps .android .tictactoe;

import android .content .Context;
import android .graphics .Canvas;
import android .graphics .Color;
import android .graphics .Paint;
import android .os .CountDownTimer;
import android .view .MotionEvent;
import android .view .View;

import java .util .Random;


public class
        Tic_Tac_Toe_View
            extends View{

    int num_of_cols = 3;
    int num_of_rows = 3;

    int  player_moves [ ][ ];

    int count_down_timer = 450;


    int width , height;

    float spacing_vertical_line;
    float spacing_horizontal_line;

    boolean turn_machine = true;
    boolean machine_first = true;
    boolean machine_got_center = false;

    final int id_player = 1;
    final int id_machine = 5;
    final int id_no_player = 0;

    final int no_winner_yet = -1;
    final int tie = 0;

    int id_winner = no_winner_yet;

    int turn_count = 1;

    public Tic_Tac_Toe_View
                (Context context ){
        super (context );
        player_moves = new int [num_of_rows ] [num_of_cols ];}

    @Override
    protected void
            onMeasure
                (int widthMeasureSpec , int heightMeasureSpec ){

        //get any width or height
        width = MeasureSpec .getSize (widthMeasureSpec );
        height =  MeasureSpec .getSize (heightMeasureSpec );

        switch (MeasureSpec .getMode (widthMeasureSpec )){
            case MeasureSpec .EXACTLY:
                break; //width must be exactly the passed width
            case MeasureSpec .AT_MOST:
                break; //width can be at most the past width
            case MeasureSpec .UNSPECIFIED:
                width = getResources ( ) .getDisplayMetrics ( ) .widthPixels; //Liberty of setting width to anything
                break;}

        switch (MeasureSpec .getMode(heightMeasureSpec )){
            case MeasureSpec .EXACTLY:
                break; //height must be exactly the passed width
            case MeasureSpec .AT_MOST:
                break; //height can be at most the past width
            case MeasureSpec .UNSPECIFIED:
                height = getResources ( ) .getDisplayMetrics ( ) .heightPixels; //Liberty of setting height to anything
                break;}

        spacing_vertical_line = (float ) width / num_of_cols;
        spacing_horizontal_line = (float ) height / num_of_rows;
        setMeasuredDimension (width, height );}




    @Override
    protected void
            onDraw
                (Canvas canvas ){
        super.onDraw(canvas);


        int stroke_width = 10;

        int lines_color = Color .rgb (1 , 48 , 83 );
        int lines_bias = 30;

        Paint line_paint = new Paint ( );
        line_paint .setColor (lines_color );
        line_paint .setStrokeWidth (stroke_width );

        //Draw Vertical lines
        canvas .drawLine (spacing_vertical_line, //x
                    0 + lines_bias ,        //y
                         spacing_vertical_line ,  //x'
                         height - lines_bias ,                //y'
                         line_paint );
        canvas .drawLine (spacing_vertical_line * 2 ,
                        0 + lines_bias ,
                        spacing_vertical_line * 2 ,
                        height - lines_bias ,
                        line_paint );

        //Draw horizontal lines
        canvas .drawLine ( 0 + lines_bias , //x
                            spacing_horizontal_line , //y
                            width - lines_bias, //x'
                            spacing_horizontal_line , //y'
                            line_paint );
        canvas .drawLine (0 +lines_bias , //x
                        spacing_horizontal_line * 2 , //y
                         width -lines_bias , //x'
                        spacing_horizontal_line * 2 , //y'
                        line_paint );

        int player_color = Color .rgb (255 ,128 ,0 );
        int computer_color = Color .DKGRAY;
        int text_color = Color .WHITE;
        int text_size = 50;
        int bias_text_h = 6;
        int bias_text_v = text_size / 2;
        int padding = 35;

        //Draw player Choices

        Paint player_paint = new Paint ( );
        player_paint .setColor (player_color );

        Paint computer_paint = new Paint ( );
        computer_paint .setColor (computer_color );

        Paint text_paint = new Paint ( );
        text_paint .setColor (text_color );
        text_paint .setTextSize (text_size );


        for (int row = 0; row < num_of_rows; row++ ){
            for (int col = 0; col < num_of_cols; col++ ){
                if (player_moves [row ][col ] == id_no_player)
                    continue;
                int x = col * (int ) spacing_vertical_line;
                int y =  row * (int ) spacing_horizontal_line;
                if (player_moves [row ][col ] == id_player){
                    canvas .drawOval (x + padding , y + padding,
                            x + spacing_vertical_line - padding ,
                            y + spacing_horizontal_line - padding ,
                            player_paint );
                    canvas .drawText("P" ,
                                    x + spacing_vertical_line / 2 - bias_text_h,
                                    y + spacing_horizontal_line / 2 + bias_text_v,
                                    text_paint);}

                else{
                    canvas .drawOval (x + padding , y + padding,
                                    x + spacing_vertical_line - padding ,
                                    y + spacing_horizontal_line - padding ,
                                     computer_paint );

                    canvas .drawText("C" ,
                                    x + spacing_vertical_line / 2 - bias_text_h,
                                    y + spacing_horizontal_line / 2 + bias_text_v,
                                    text_paint); }}}

        switch (id_winner ){
            case id_player:
            case id_machine:
            case tie:
                String message = id_winner == id_player ? "Player won" : id_winner == id_machine ? "Machine won " : "TIE";
                int bias_rect = 40;
                int bias_text = 120;
                id_winner = no_winner_yet;
                player_moves = new int [num_of_rows ] [num_of_cols ];
                machine_first = false;
                machine_got_center = false;
                turn_count = 1;
                canvas.drawRect (0 , height / 2 - bias_rect,  width , height / 2 + bias_rect , line_paint );
                canvas .drawText(message ,
                        0 + width / 2 - bias_text,
                        0 + height / 2 + bias_rect / 2,
                        text_paint );
                break;
            case no_winner_yet:
                break; }
        if (turn_count == 1 && turn_machine ){
            new CountDownTimer( count_down_timer , 100 ){
                public void onTick(long millisUntilFinished ) { }
                public void onFinish( ){
                    machine_play( );}} .start ( );}}

    @Override
    public boolean
            onTouchEvent
                (MotionEvent event ){

        if (event .getAction ( ) == MotionEvent .ACTION_DOWN ){
            int x = (int ) event .getX ( );
            int y = (int ) event .getY ( );

            int col = x / (int ) spacing_vertical_line;
            int row = y / (int ) spacing_horizontal_line;

            if (is_tie( ) ){
                id_winner = tie;
                invalidate ( ); }
            else{
                if (player_moves [row ][col ] == id_no_player && !turn_machine){
                    player_moves [row ][col ] = id_player;
                    turn_machine = !turn_machine;
                    turn_count++;
                    invalidate ( );
                    new CountDownTimer ( count_down_timer , 100 ){
                        public void onTick (long millisUntilFinished ) { }
                        public void onFinish ( ){
                            if (is_tie( ) ){
                                id_winner = tie;
                                invalidate ( ); }
                            else{
                                machine_play( );
                                if (is_tie( ) ){
                                    id_winner = tie;
                                    invalidate ( ); }}}} .start ( );}}
            return true;}
        return false;}


    private void
            machine_play ( ){
        if (turn_machine && turn_count == 1 )
            machine_first = true;

        if (turn_machine && machine_first )
            machine_started_play ( );
        else if (turn_machine && !machine_first )
            player_started_play(); }

    private void
            machine_started_play
                ( ){

        int coordinates [ ] = null;
        int machine_row_open_for_win = -1 ;
        int machine_col_open_for_win = -1 ;

        if (turn_count == 1 )//machine is starting
            coordinates  = get_random_free_corner ( );

        else if (turn_count != 1 ){ // machine is starting , and has started
            coordinates = where_a_player_might_win(id_machine);
            if (coordinates != null ){
                id_winner = id_machine;}
            else{
                coordinates = where_a_player_might_win(id_player);
                if (coordinates == null ){
                    machine_row_open_for_win = get_row_where_possibility_of_win(id_machine);
                    machine_col_open_for_win = get_col_where_possibility_of_win(id_machine);
                    if (machine_row_open_for_win != -1 )
                        coordinates = get_random_corner_on_a_row(machine_row_open_for_win );
                    else if (machine_col_open_for_win != -1 ){
                        coordinates = get_random_corner_on_a_col(machine_col_open_for_win ); }
                    else
                        coordinates = get_any_open_coordinate ( );}}}

        player_moves [coordinates [0 ]] [coordinates [1 ]] = id_machine;
        turn_machine = !turn_machine;
        turn_count++;
        invalidate ( ); }

    private void
            player_started_play
                ( ){

        int coordinates [ ] = null;
        int machine_row_open_for_win = -1 ;
        int machine_col_open_for_win = -1 ;
        int player_row_open_for_win = -1 ;
        int player_col_open_for_win = -1;

        if (!is_center_occupied ( ) ){
            machine_got_center = true;
            coordinates  = center_coordinates ( ); }

        if (machine_got_center && turn_count != 2 ){
            coordinates = where_a_player_might_win(id_machine);
            if (coordinates != null ){
                id_winner = id_machine; }
            else{
                coordinates = where_a_player_might_win(id_player);
                if (coordinates == null ){
                    machine_row_open_for_win = get_row_where_possibility_of_win (id_machine );
                    player_row_open_for_win = get_row_where_possibility_of_win (id_player );
                    player_col_open_for_win = get_col_where_possibility_of_win (id_player );
                    if (machine_row_open_for_win != -1 )
                        coordinates = get_any_free_coordinate_from_a_row(machine_row_open_for_win );
                    else if (player_row_open_for_win != -1 &&
                             player_col_open_for_win != -1 &&
                             player_moves [player_row_open_for_win ] [player_col_open_for_win ] == id_no_player ){
                        coordinates = new int [ ]{player_row_open_for_win , player_col_open_for_win }; }
                    else
                        coordinates = get_any_open_coordinate ( );}}}
        else if (!machine_got_center ){
            coordinates = where_a_player_might_win (id_machine );
            if (coordinates != null ){
                id_winner = id_machine; }
            else{
                coordinates = where_a_player_might_win (id_player );
                if (coordinates == null ){
                    machine_row_open_for_win = get_row_where_possibility_of_win(id_machine);
                    machine_col_open_for_win = get_row_where_possibility_of_win(id_machine);
                    if (is_there_a_free_corner( ) )
                        coordinates = get_random_free_corner( );
                    else if (machine_row_open_for_win != -1 )
                        coordinates = get_any_free_coordinate_from_a_row (machine_row_open_for_win );
                    else if (machine_col_open_for_win != -1 )
                        coordinates = get_any_free_coordinate_from_a_col (machine_col_open_for_win );
                    else
                        coordinates = get_any_open_coordinate( );}}}

        player_moves [coordinates [0 ]] [coordinates [1 ]] = id_machine;
        turn_machine = !turn_machine;
        turn_count++;
        invalidate ( );}

    private int
            get_row_where_possibility_of_win
                (int id ){
        int row = 0;
        int col = 0;
        int sum = 0;
        int possibility_of_winning_row =  id ;
        for ( row = 0 ; row < num_of_rows ; row++ ){
            sum = 0;
            for ( col = 0 ; col < num_of_cols ; col++ ){
                sum += player_moves [row ][col ]; }
            if (sum == possibility_of_winning_row ){
                return row ;}}
        return -1;}

    private int
            get_col_where_possibility_of_win
                (int id ){
        int row = 0;
        int col = 0;
        int sum = 0;
        int possibility_of_winning_col =  id ;
        for (col = 0 ; col < num_of_cols ; col++ ){
            sum = 0;
            for (row = 0 ; row < num_of_rows ; row++ ){
                sum += player_moves [row ][col ];}
            if (sum == possibility_of_winning_col ){
                return col ;}}
        return -1;}


    private boolean
            is_center_occupied
                ( ){
        if (player_moves [num_of_rows / 2 ] [num_of_cols / 2 ] != id_no_player ){
            return true;}
        return false;}

    private int [ ]
        center_coordinates
            ( ){
        return new int [ ]{ num_of_rows / 2 , num_of_cols / 2 };}

    private boolean
            is_there_a_free_corner
                ( ){
        if (
                player_moves [0 ] [0 ] == id_no_player ||
                player_moves [0 ] [num_of_cols - 1 ] == id_no_player ||
                player_moves [num_of_rows - 1 ] [0 ] == id_no_player ||
                player_moves [num_of_rows - 1 ] [num_of_cols - 1 ] == id_no_player )
            return true;
        return false;}

    private int []
            get_random_free_corner
                ( ){
        int number_of_corners = 4;
        int  corners [ ][ ] = { { 0 , 0 } ,
                                { 0 , num_of_cols - 1 } ,
                                { num_of_rows - 1 , 0 } ,
                                { num_of_rows - 1 , num_of_cols - 1 } };

        int chosen_corner = new Random ( ) .nextInt (number_of_corners );
        int row = corners [chosen_corner ][0 ];
        int col = corners [chosen_corner ][1 ];
        if (player_moves [row ] [col ] == id_no_player )
            return corners [chosen_corner ];
        return get_random_free_corner ( );}

    private int []
            get_random_corner_on_a_row
                (int row ){
        int coordinates [ ] = get_random_free_corner ( );
        if (coordinates [0 ] == row )
            return coordinates;
        return get_random_corner_on_a_row (row );}


    private int []
            get_random_corner_on_a_col
                (int col ){
        int coordinates [ ] = get_random_free_corner ( );
        if (coordinates [1 ] == col )
            return coordinates;
        return get_random_corner_on_a_col (col );}


    private int []
            get_any_free_coordinate_from_a_row
                (int row ) {

        int coordinates [ ] = {-1, -1 };

        for (int col = 0; col < num_of_cols ; col++ ){
            if (player_moves [row ][col ] == id_no_player){
                coordinates [0 ] = row;
                coordinates [1 ] = col;
                return coordinates; } }

        return null; }


    private int []
    get_any_free_coordinate_from_a_col
            (int col ) {

        int coordinates [ ] = {-1, -1 };

        for (int row = 0; row < num_of_rows ; row++ ){
            if (player_moves [row ][col ] == id_no_player){
                coordinates [0 ] = row;
                coordinates [1 ] = col;
                return coordinates; } }

        return null; }


    private int [ ]
            where_a_player_might_win
                (int id ){
        int coordinates [ ] = { -1 , -1 };

        int possibility_of_winning_on_a_row = id == id_player ? id_player * (num_of_cols - 1 ) : id_machine * (num_of_cols - 1 );
        int possibility_of_winning_on_a_col = id == id_player ? id_player * (num_of_rows - 1 ) : id_machine * (num_of_rows - 1 ) ;

        int sum = 0;
        int row = 0;
        int col = 0;

        int col_might_win = -1;
        int row_might_win = -1;

        //Check rows
        for (row = 0 ; row < num_of_rows ; row++ ){
            sum = 0;
            col_might_win = -1;
            for (col = 0 ; col < num_of_cols ; col++ ){
                sum += player_moves [row ][col ];
                if (player_moves [row ][col ] == id_no_player)
                    col_might_win = col; }
            if (sum == possibility_of_winning_on_a_row ){
                coordinates [0 ] = row;
                coordinates [1 ] = col_might_win;
                return coordinates; }}

        //Check cols
        sum = 0;
        for (col = 0 ; col < num_of_cols ; col++ ){
            sum = 0;
            row_might_win = -1;
            for (row = 0 ; row < num_of_rows ; row++ ){
                sum += player_moves [row ][col ];
                if (player_moves [row ][col ] == id_no_player)
                    row_might_win = row; }
            if (sum == possibility_of_winning_on_a_col ){
                coordinates [0 ] = row_might_win;
                coordinates [1 ] = col;
                return coordinates; }}

        // check diagonally
        if (num_of_cols == num_of_rows ){
            sum = 0;
            row_might_win = -1;
            col_might_win = -1;
            for (row = 0 , col =0 ; row < num_of_rows ; row++, col++ ){
                    sum += player_moves [row ][col ];
                    if (player_moves [row ] [col ] == id_no_player){
                        row_might_win = row;
                        col_might_win = col; }}

            if (sum == possibility_of_winning_on_a_row ){
                coordinates [0 ] = row_might_win;
                coordinates [1 ] = col_might_win;
                return coordinates;}

            sum = 0;
            row_might_win = -1;
            col_might_win = -1;
            for (row = 0 , col =num_of_cols - 1 ; row < num_of_rows ; row++, col-- ){
                sum += player_moves [row ][col ];
                if (player_moves [row ] [col ] == 0 ){
                    row_might_win = row;
                    col_might_win = col;}}

            if (sum == possibility_of_winning_on_a_row ){
                coordinates [0 ] = row_might_win;
                coordinates [1 ] = col_might_win;
                return coordinates; } }
        return null; }

    public boolean
            is_tie
                ( ){
        int row = 0;
        int col = 0;
        for (row = 0 ; row < num_of_rows ; row++ )
            for (col = 0 ; col < num_of_cols ; col++ )
                if (player_moves [row ][col ] == id_no_player )
                    return false;
        return true; }

    public int [ ]
            get_any_open_coordinate
                ( ){
        int row = 0;
        int col = 0;
        int coordinates [ ] = {-1 , -1 };
        for (row = 0 ; row < num_of_rows ; row++ )
            for (col = 0 ; col < num_of_cols ; col++ )
                if (player_moves [row ][col ] == id_no_player){
                    coordinates [0 ] = row;
                    coordinates [1 ] = col;
                    return coordinates; }
        return null; }}


