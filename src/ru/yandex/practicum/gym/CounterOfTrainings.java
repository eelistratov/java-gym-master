package ru.yandex.practicum.gym;

public class CounterOfTrainings {
    private final Coach coach;  // Тренер
    private final int count;     // Количество тренировок за неделю

    public CounterOfTrainings(Coach coach, int count) {
        this.coach = coach;
        this.count = count;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("%s %s.%s. - %d тренировок",
                coach.getSurname(),
                coach.getName().charAt(0),
                coach.getMiddleName().charAt(0),
                count);
    }
}
