import java.util.Scanner;

public class SolutionWithCountingSort {
  private static int[] frequency;
  private static int[] trailingDays_sorted;
  private static final int MAX_EXPENDITURE_PER_DAY = 200;

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    int days_withTransactionData = scanner.nextInt();
    int trailingDays = scanner.nextInt();
    int[] expenditure = new int[days_withTransactionData];

    for (int i = 0; i < days_withTransactionData; i++) {
      expenditure[i] = scanner.nextInt();
    }
    scanner.close();

    int total_notifications = find_numberOfNotifications(trailingDays, expenditure);
    System.out.println(total_notifications);
  }

  private static int find_numberOfNotifications(int trailingDays, int[] expenditure) {

    int notifications = 0;
    frequency = new int[MAX_EXPENDITURE_PER_DAY + 1];
    trailingDays_sorted = new int[trailingDays];

    for (int i = 0; i < trailingDays; i++) {
      frequency[expenditure[i]]++;
    }

    countingSort_trailingDays();
    double median = findMedian();
    notifications = expenditure[trailingDays] >= 2 * median ? notifications + 1 : notifications;

    int index_oldestTrailingDayValue = 0;
    for (int i = trailingDays; i < expenditure.length - 1; i++) {

      int oldestTrailingDayValue = expenditure[index_oldestTrailingDayValue];
      int newestTrailingDayValue = expenditure[i];

      index_oldestTrailingDayValue++;
      frequency[oldestTrailingDayValue]--;
      frequency[newestTrailingDayValue]++;

      countingSort_trailingDays();
      median = findMedian();
      notifications = expenditure[i + 1] >= 2 * median ? notifications + 1 : notifications;
    }

    return notifications;
  }

  private static double findMedian() {
    int length = trailingDays_sorted.length;
    return length % 2 != 0  ? (double) trailingDays_sorted[length / 2]
                            : ((double) trailingDays_sorted[(length / 2) - 1] + (double) trailingDays_sorted[length / 2]) / 2;
  }

  private static void countingSort_trailingDays() {
    int index_sorted = 0;
    for (int i = 0; i < frequency.length; i++) {

      int freq = frequency[i];
      while (freq > 0) {
        trailingDays_sorted[index_sorted] = i;
        freq--;
        index_sorted++;
      }
    }
  }
}
