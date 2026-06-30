import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JavaGradesHelper implements GradesHelper {

	private final Map<Name, Integer> students = new HashMap<>();
	private final Map<String, Band> bandByMark = new HashMap<>();
	private final List<Band> bands = new ArrayList<>();

	@Override
	public void loadStudents(String file) {
		students.clear();
		Path path = Path.of(file);
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String trimmed = line == null ? null : line.trim();
				if (trimmed == null || trimmed.isEmpty()) {
					continue;
				}

				String[] cols = trimmed.split(";");
				if (cols.length < 3) {
					continue;
				}

				String idText = cols[0].trim();
				String first = cols[1].trim();
				String last = cols[2].trim();
				if (idText.isEmpty() || first.isEmpty() || last.isEmpty()) {
					continue;
				}

				try {
					int id = Integer.parseInt(idText);
					students.put(new Name(first, last), id);
				} catch (NumberFormatException ignored) {
					
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read students file", e);
		}
	}

	@Override
	public void loadScoring(String file) throws RangeConflictException, MarkConflictException {
		bands.clear();
		bandByMark.clear();

		Path path = Path.of(file);
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String trimmed = line == null ? null : line.trim();
				if (trimmed == null || trimmed.isEmpty()) {
					continue;
				}

				String[] cols = trimmed.split(";");
				if (cols.length < 3) {
					continue;
				}

				String mark = cols[0].trim();
				String fromTxt = cols[1].trim();
				String toTxt = cols[2].trim();
				if (mark.isEmpty() || fromTxt.isEmpty() || toTxt.isEmpty()) {
					continue;
				}

				double from;
				double to;
				try {
					from = parseNumber(fromTxt);
					to = parseNumber(toTxt);
				} catch (NumberFormatException ignored) {
					continue;
				}

				Band candidate = new Band(mark, from, to);
				Band existing = bandByMark.get(mark);
				if (existing != null) {
					if (!existing.sameAs(candidate)) {
						throw new MarkConflictException(mark);
					} else {
						continue;
					}
				}

				bands.add(candidate);
				bandByMark.put(mark, candidate);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read scoring file", e);
		}

		Collections.sort(bands, Comparator.comparingDouble(b -> b.from));
		for (int i = 1; i < bands.size(); i++) {
			Band prev = bands.get(i - 1);
			Band curr = bands.get(i);
			if (rangesOverlap(prev.from, prev.to, curr.from, curr.to)) {
				throw new RangeConflictException();
			}
		}
	}

	@Override
	public Map<Integer, String> generateGrades(String data) throws AssessmentImpossible {
		Map<Integer, String> result = new LinkedHashMap<>();
		Path path = Path.of(data);
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String trimmed = line == null ? null : line.trim();
				if (trimmed == null || trimmed.isEmpty()) {
					continue;
				}

				String[] cols = trimmed.split(";");
				if (cols.length < 3) {
					continue;
				}

				String first = cols[0].trim();
				String last = cols[1].trim();
				if (first.isEmpty() || last.isEmpty()) {
					continue;
				}

				Integer id = students.get(new Name(first, last));
				if (id == null) {
					throw new AssessmentImpossible(first, last);
				}

				double total = 0.0;
				int taken = 0;
				for (int i = 2; i < cols.length; i++) {
					String scoreTxt = cols[i].trim();
					if (scoreTxt.isEmpty()) {
						continue;
					}
					try {
						total += parseNumber(scoreTxt);
						taken++;
					} catch (NumberFormatException ex) {
						throw new AssessmentImpossible(first, last);
					}
				}

				if (taken == 0) {
					throw new AssessmentImpossible(first, last);
				}

				double avg = total / taken;
				String mark = findMarkFor(avg);
				if (mark == null) {
					throw new AssessmentImpossible(first, last);
				}
				result.put(id, mark);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read grades file", e);
		}

		return result;
	}

	private static boolean rangesOverlap(double aFrom, double aTo, double bFrom, double bTo) {
		return aFrom <= bTo && bFrom <= aTo;
	}

	private static double parseNumber(String text) {
		return Double.parseDouble(text.replace(',', '.'));
	}

	private String findMarkFor(double value) {
		int lo = 0, hi = bands.size() - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			Band b = bands.get(mid);
			if (value < b.from) {
				hi = mid - 1;
			} else if (value > b.to) {
				lo = mid + 1;
			} else {
				return b.mark;
			}
		}
		return null;
	}

	private static final class Name {
		private final String first;
		private final String last;

		Name(String first, String last) {
			this.first = normalize(first);
			this.last = normalize(last);
		}

		private static String normalize(String s) {
			return s == null ? "" : s.trim();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Name)) return false;
			Name name = (Name) o;
			return Objects.equals(first, name.first) && Objects.equals(last, name.last);
		}

		@Override
		public int hashCode() {
			return Objects.hash(first, last);
		}
	}

	private static final class Band {
		final String mark;
		final double from;
		final double to;

		Band(String mark, double from, double to) {
			this.mark = mark;
			this.from = from;
			this.to = to;
		}

		boolean sameAs(Band other) {
			return other != null
					&& Objects.equals(this.mark, other.mark)
					&& Double.compare(this.from, other.from) == 0
					&& Double.compare(this.to, other.to) == 0;
		}
	}
}
