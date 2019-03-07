
forced = [(1, “A”), (2, “B”)]
accepted = []



getAllCombs :: [char] -> [[char]]


-- machine = “ABCDEFGH”

stringCount :: [char] -> char -> [int]
stringCount str ch = [ y | (x, y) <- zip str [0..], x == ch ]


stringIsAccepted :: List -> [char] -> bool
stringIsAccepted [] machine -> true
stringIsAccepted (x:xs) machine -> stringCount machine snd x == fst x && stringIsAccepted xs machine




getFAccepted :: List -> [[char]] -> List -> List
accepted = []

getFAccepted [] (y:ys) accepted = (y:ys)
getFAccepted (x:xs) [] accepted = []
getFAccepted (x:xs) (y:ys) accepted = 
	if stringIsAccepted (x:xs) y 
		then accepted = accepted ++ y
	else
		getFAccepted (x:xs) ys accepted
	getFAccepted (x:xs) ys accepted
	then
		accepted

