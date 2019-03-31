% file io
read_file(Stream, []) :-
    at_end_of_stream(Stream).
read_file(Stream, [X|L]) :-
    %catch(\+ at_end_of_stream(Stream), E, close(Stream)),
        \+ at_end_of_stream(Stream),
    readLine(Stream, X),
    read_file(Stream, L).
readLine(InStream, W) :-
    get_code(InStream, Char),
    checkCharAndReadRest(Char, Chars, InStream),
    atom_codes(W, Chars).
    
checkCharAndReadRest(10,[],_) :- !.
checkCharAndReadRest(13,[],_) :- !.
checkCharAndReadRest(-1,[],_) :- !.
checkCharAndReadRest(0, [],_) :- !.
checkCharAndReadRest(end_of_file,[],_) :- !.
checkCharAndReadRest(Char, [Char|Chars], InStream) :-
    get_code(InStream, NextChar),
    checkCharAndReadRest(NextChar, Chars, InStream).

checkHeadersValidity(Text) :-
    member('Name:', Text),
    member('forced partial assignment:', Text),
    member('forbidden machine:', Text),
    member('too-near tasks:', Text),
    member('machine penalties:', Text),
    member('too-near penalities', Text).

areHeadersValid(X) :-
    checkHeadersValidity(X),
    print("Valid Headers").




main(Argv) :-
    nth0(0, Argv, Argument1),
    nth0(1, Argv, Argument2),
    open(Argument1, read, S),
    read_file(S, Lines),
    %print(Lines),
    areHeadersValid(Lines),
    close(S).
    %write("file read pass"), nl.