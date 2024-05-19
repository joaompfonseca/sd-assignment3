xterm -T "General Repository" -hold -e "./general_repository.sh" &
read -p "Press ENTER when General Repository is ready..."

xterm -T "Contestants Bench" -hold -e "./contestants_bench.sh" &
xterm -T "Playground" -hold -e "./playground.sh" &
xterm -T "Referee Site" -hold -e "./referee_site.sh" &
read -p "Press ENTER when all servers are ready..."

xterm -T "Coach" -hold -e "./coach.sh" &
xterm -T "Contestant" -hold -e "./contestant.sh" &
xterm -T "Referee" -hold -e "./referee.sh" &